package com.cyberspectraa.cyberclasses.classdata;

import com.cyberspectraa.cyberclasses.network.CyberClassesNetwork;
import com.cyberspectraa.cyberclasses.network.packet.CloseClassAdvancementPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Class advancement no longer interrupts the player at Level 20.
 * A compatible Rare/Legendary Class Book is now required to advance.
 * Existing saved advancements and operator-controlled advancements persist.
 */
public final class ClassAdvancementManager {
    private ClassAdvancementManager() {}

    public static void handleLogin(ServerPlayer player) {
        // Intentionally no forced popup at the level threshold.
    }

    public static void tick(ServerPlayer player) {
        // Books trigger advancement on use; no per-tick window checks.
    }

    public static void openSelection(ServerPlayer player) {
        if (player != null) {
            player.sendSystemMessage(Component.literal(
                    "Class advancement now requires a Rare or Legendary Class Book. "
                    + "Hunt monsters and powerful bosses to find them.")
                    .withStyle(ChatFormatting.YELLOW));
        }
    }

    /** Legacy GUI packets cannot bypass the book and rarity requirements. */
    public static boolean complete(ServerPlayer player, ClassAdvancement advancement) {
        return false;
    }

    public static void forceSet(ServerPlayer player, ClassAdvancement advancement) {
        if (player == null || advancement == null) return;
        ClassManager.forceSetAdvancement(player, advancement);
        CyberClassesNetwork.sendToPlayer(player, new CloseClassAdvancementPacket());
    }

    public static void reset(ServerPlayer player) {
        if (player == null) return;
        ClassManager.clearAdvancement(player);
        CyberClassesNetwork.sendToPlayer(player, new CloseClassAdvancementPacket());
    }

    public static void onLogout(ServerPlayer player) {
        // No temporary state; class data is persistent.
    }
}
