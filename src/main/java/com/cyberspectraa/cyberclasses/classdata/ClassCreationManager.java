package com.cyberspectraa.cyberclasses.classdata;

import com.cyberspectraa.cyberclasses.network.CyberClassesNetwork;
import com.cyberspectraa.cyberclasses.network.packet.CloseClassCreatorPacket;
import com.cyberspectraa.cyberclasses.network.packet.OpenClassCreatorPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;
import java.lang.reflect.Method;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class ClassCreationManager {
    private static final String RACE_ROOT = "CyberRaces";
    private static final String RACE_KEY = "Race";
    private static final String RACE_CREATED_KEY = "CharacterCreated";

    private static final Set<UUID> OPEN_SENT =
        new HashSet<>();
    private static final Map<UUID, Long> GUILD_SELECTION = new HashMap<>();
    private static final long GUILD_SESSION_TICKS = 2400L;

    private ClassCreationManager() {
    }

    public static void handleLogin(ServerPlayer player) {
        if (ClassManager.hasClass(player)) {
            CreationHoldManager.release(player);
            return;
        }

        // When CyberNpc's town intro is configured, class selection belongs
        // to the Adventurers' Guild AFTER the summoning and Pope meeting.
        if (hasCompletedRace(player) && !guildRegistrationEnabled(player)) {
            beginSelection(player);
        }
    }

    public static void tick(ServerPlayer player) {
        if (ClassManager.hasClass(player)) {
            OPEN_SENT.remove(player.getUUID());
            return;
        }

        if (!hasCompletedRace(player)) {
            return;
        }

        if (guildRegistrationEnabled(player)) {
            // A classless player may explore town without being immobilised.
            return;
        }
        CreationHoldManager.tick(player);

        if (!OPEN_SENT.contains(player.getUUID())) {
            beginSelection(player);
        }
    }

    /**
     * Reflection entry point used by CyberNpc's Guild Receptionist.
     * Only a completed intro can authorise this server-side session.
     */
    public static boolean openAtGuild(ServerPlayer player) {
        if (player == null || ClassManager.hasClass(player)
                || !hasCompletedRace(player)
                || !guildRegistrationAvailable(player)) return false;
        GUILD_SELECTION.put(player.getUUID(),
                player.level().getGameTime() + GUILD_SESSION_TICKS);
        OPEN_SENT.add(player.getUUID());
        CyberClassesNetwork.sendToPlayer(player, new OpenClassCreatorPacket(true));
        return true;
    }

    public static void complete(
        ServerPlayer player,
        CyberClass playerClass,
        boolean starterKit
    ) {
        if (player == null || playerClass == null || !playerClass.playerSelectable()
                || ClassManager.hasClass(player) || !hasCompletedRace(player)) return;

        boolean guildMode = guildRegistrationEnabled(player);
        if (guildMode) {
            Long expires = GUILD_SELECTION.get(player.getUUID());
            if (expires == null || player.level().getGameTime() > expires
                    || !guildRegistrationAvailable(player)) return;
        } else if (!OPEN_SENT.contains(player.getUUID())) {
            return;
        }

        ClassManager.setClass(player, playerClass);
        if (!ClassManager.hasClass(player)) return;
        OPEN_SENT.remove(player.getUUID());
        GUILD_SELECTION.remove(player.getUUID());
        StarterKitService.complete(player, playerClass, starterKit);
        CyberClassesNetwork.sendToPlayer(
            player,
            new CloseClassCreatorPacket()
        );
        if (!guildMode) {
            // Worlds without the configured CyberNpc introduction preserve
            // the original class-before-summoning flow.
            if (summoningHandoff(player)) CreationHoldManager.forget(player);
            else CreationHoldManager.release(player);
        }
    }

    // Compatibility for older internal callsites; new player packets carry
    // the kit decision explicitly.
    public static void complete(ServerPlayer player, CyberClass playerClass) {
        complete(player, playerClass, true);
    }

    public static void forceSet(
        ServerPlayer player,
        CyberClass playerClass
    ) {
        ClassManager.setClass(player, playerClass);
        OPEN_SENT.remove(player.getUUID());
        GUILD_SELECTION.remove(player.getUUID());
        CreationHoldManager.release(player);
        CyberClassesNetwork.sendToPlayer(
            player,
            new CloseClassCreatorPacket()
        );
    }

    public static void reset(ServerPlayer player) {
        ClassManager.clearClass(player);
        OPEN_SENT.remove(player.getUUID());
        GUILD_SELECTION.remove(player.getUUID());

        if (hasCompletedRace(player) && !guildRegistrationEnabled(player)) {
            beginSelection(player);
        }
    }

    public static void onLogout(ServerPlayer player) {
        OPEN_SENT.remove(player.getUUID());
        GUILD_SELECTION.remove(player.getUUID());
        CreationHoldManager.forget(player);
    }

    public static boolean hasCompletedRace(ServerPlayer player) {
        CompoundTag persistent = player.getPersistentData();

        if (!persistent.contains(RACE_ROOT)) {
            return false;
        }

        CompoundTag root =
            persistent.getCompound(RACE_ROOT);

        if (!root.contains(RACE_KEY)
                || root.getString(RACE_KEY).isBlank()) {
            return false;
        }

        return !root.contains(RACE_CREATED_KEY)
            || root.getBoolean(RACE_CREATED_KEY);
    }

    private static boolean guildRegistrationEnabled(ServerPlayer player) {
        return introBoolean(player, "guildRegistrationEnabled");
    }

    private static boolean guildRegistrationAvailable(ServerPlayer player) {
        return introBoolean(player, "guildRegistrationAvailable");
    }

    private static boolean introBoolean(ServerPlayer player, String method) {
        if (player == null || !ModList.get().isLoaded("cybernpc")) return false;
        try {
            Class<?> intro = Class.forName(
                    "com.cyberspectraa.cybernpc.intro.CyberIntroService");
            return Boolean.TRUE.equals(
                    intro.getMethod(method, ServerPlayer.class).invoke(null, player));
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            return false;
        }
    }

    private static boolean summoningHandoff(ServerPlayer player) {
        if (!ModList.get().isLoaded("cybernpc")) return false;
        try {
            Class<?> intro = Class.forName("com.cyberspectraa.cybernpc.intro.CyberIntroService");
            Method method = intro.getMethod("beginSummoning", ServerPlayer.class);
            return Boolean.TRUE.equals(method.invoke(null, player));
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            return false;
        }
    }

    private static void beginSelection(ServerPlayer player) {
        CreationHoldManager.enter(player);
        OPEN_SENT.add(player.getUUID());
        CyberClassesNetwork.sendToPlayer(
            player,
            new OpenClassCreatorPacket()
        );
    }
}
