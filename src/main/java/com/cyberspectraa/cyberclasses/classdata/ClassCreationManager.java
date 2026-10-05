package com.cyberspectraa.cyberclasses.classdata;

import com.cyberspectraa.cyberclasses.network.CyberClassesNetwork;
import com.cyberspectraa.cyberclasses.network.packet.CloseClassCreatorPacket;
import com.cyberspectraa.cyberclasses.network.packet.OpenClassCreatorPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class ClassCreationManager {
    private static final String RACE_ROOT = "CyberRaces";
    private static final String RACE_KEY = "Race";
    private static final String RACE_CREATED_KEY = "CharacterCreated";

    private static final Set<UUID> OPEN_SENT =
        new HashSet<>();

    private ClassCreationManager() {
    }

    public static void handleLogin(ServerPlayer player) {
        if (ClassManager.hasClass(player)) {
            CreationHoldManager.release(player);
            return;
        }

        if (hasCompletedRace(player)) {
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

        CreationHoldManager.tick(player);

        if (!OPEN_SENT.contains(player.getUUID())) {
            beginSelection(player);
        }
    }

    public static void complete(
        ServerPlayer player,
        CyberClass playerClass
    ) {
        if (playerClass == null || ClassManager.hasClass(player)) {
            return;
        }

        ClassManager.setClass(player, playerClass);
        OPEN_SENT.remove(player.getUUID());
        CreationHoldManager.release(player);
        CyberClassesNetwork.sendToPlayer(
            player,
            new CloseClassCreatorPacket()
        );
    }

    public static void forceSet(
        ServerPlayer player,
        CyberClass playerClass
    ) {
        ClassManager.setClass(player, playerClass);
        OPEN_SENT.remove(player.getUUID());
        CreationHoldManager.release(player);
        CyberClassesNetwork.sendToPlayer(
            player,
            new CloseClassCreatorPacket()
        );
    }

    public static void reset(ServerPlayer player) {
        ClassManager.clearClass(player);
        OPEN_SENT.remove(player.getUUID());

        if (hasCompletedRace(player)) {
            beginSelection(player);
        }
    }

    public static void onLogout(ServerPlayer player) {
        OPEN_SENT.remove(player.getUUID());
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

    private static void beginSelection(ServerPlayer player) {
        CreationHoldManager.enter(player);
        OPEN_SENT.add(player.getUUID());
        CyberClassesNetwork.sendToPlayer(
            player,
            new OpenClassCreatorPacket()
        );
    }
}
