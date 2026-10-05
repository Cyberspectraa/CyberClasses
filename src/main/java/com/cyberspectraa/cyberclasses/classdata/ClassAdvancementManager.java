package com.cyberspectraa.cyberclasses.classdata;

import com.cyberspectraa.cyberclasses.network.CyberClassesNetwork;
import com.cyberspectraa.cyberclasses.network.packet.CloseClassAdvancementPacket;
import com.cyberspectraa.cyberclasses.network.packet.OpenClassAdvancementPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class ClassAdvancementManager {
    private static final Set<UUID> PROMPTED =
        new HashSet<>();

    private ClassAdvancementManager() {
    }

    public static void handleLogin(ServerPlayer player) {
        PROMPTED.remove(player.getUUID());
        tryPrompt(player);
    }

    public static void tick(ServerPlayer player) {
        if (player == null
                || player.tickCount % 20 != 0
                || !ClassManager.isAdvancementEligible(player)
                || PROMPTED.contains(player.getUUID())) {
            return;
        }

        tryPrompt(player);
    }

    public static void openSelection(ServerPlayer player) {
        CyberClass baseClass =
            ClassManager.getClass(player).orElse(null);

        if (baseClass == null
                || !ClassManager.isAdvancementEligible(player)) {
            return;
        }

        PROMPTED.add(player.getUUID());
        CyberClassesNetwork.sendToPlayer(
            player,
            new OpenClassAdvancementPacket(
                baseClass.id()
            )
        );
    }

    public static boolean complete(
        ServerPlayer player,
        ClassAdvancement advancement
    ) {
        if (!ClassManager.setAdvancement(
                player,
                advancement
        )) {
            return false;
        }

        PROMPTED.remove(player.getUUID());

        CyberClassesNetwork.sendToPlayer(
            player,
            new CloseClassAdvancementPacket()
        );

        player.sendSystemMessage(
            Component.literal("Class Advanced: ")
                .withStyle(ChatFormatting.GOLD)
                .append(
                    Component.literal(
                        advancement.displayName()
                    ).withStyle(ChatFormatting.YELLOW)
                )
        );

        return true;
    }

    public static void forceSet(
        ServerPlayer player,
        ClassAdvancement advancement
    ) {
        ClassManager.forceSetAdvancement(
            player,
            advancement
        );
        PROMPTED.remove(player.getUUID());

        CyberClassesNetwork.sendToPlayer(
            player,
            new CloseClassAdvancementPacket()
        );
    }

    public static void reset(ServerPlayer player) {
        ClassManager.clearAdvancement(player);
        PROMPTED.remove(player.getUUID());

        if (ClassManager.isAdvancementEligible(player)) {
            openSelection(player);
        }
    }

    public static void onLogout(ServerPlayer player) {
        if (player != null) {
            PROMPTED.remove(player.getUUID());
        }
    }

    private static void tryPrompt(ServerPlayer player) {
        if (ClassManager.isAdvancementEligible(player)) {
            openSelection(player);
        }
    }
}
