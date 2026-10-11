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
        // Class advancement is now obtained through rarity-based Class Books.
        // Existing advancements remain untouched on login.
    }

    public static void tick(ServerPlayer player) {
        // No forced Level 20 selection screen. Progression is book-driven.
    }

    public static void openSelection(ServerPlayer player) {
        CyberClass baseClass =
            ClassManager.getClass(player).orElse(null);

        if (baseClass == null
                || !ClassManager.isAdvancementEligible(player)) {
            return;
        }

        player.sendSystemMessage(Component.literal(
                "Advanced classes now require a Rare or Legendary Class Book. "
                + "Books are found as monster loot and require their own Cyber Level.")
                .withStyle(ChatFormatting.YELLOW));
    }

    public static boolean complete(
        ServerPlayer player,
        ClassAdvancement advancement
    ) {
        // Reject legacy/unprompted GUI submissions. Books use the validated
        // ClassBookItem path instead of this old level-up button flow.
        return false;
        /*
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
        */
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

        // Clearing advancement does not auto-reopen the old chooser.
    }

    public static void onLogout(ServerPlayer player) {
        if (player != null) {
            PROMPTED.remove(player.getUUID());
        }
    }

    // Admin tools can still force-set advancements without consuming books.
}
