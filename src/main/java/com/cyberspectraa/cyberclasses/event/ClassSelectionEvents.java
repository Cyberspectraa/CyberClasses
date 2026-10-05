package com.cyberspectraa.cyberclasses.event;

import com.cyberspectraa.cyberclasses.classdata.ClassAdvancementManager;
import com.cyberspectraa.cyberclasses.classdata.ClassCreationManager;
import com.cyberspectraa.cyberclasses.classdata.ClassManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ClassSelectionEvents {
    private ClassSelectionEvents() {
    }

    @SubscribeEvent
    public static void onLogin(
        PlayerEvent.PlayerLoggedInEvent event
    ) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ClassCreationManager.handleLogin(player);

            if (ClassManager.hasClass(player)) {
                ClassAdvancementManager.handleLogin(player);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(
        TickEvent.PlayerTickEvent event
    ) {
        if (event.phase != TickEvent.Phase.END
                || !(event.player instanceof ServerPlayer player)) {
            return;
        }

        ClassCreationManager.tick(player);

        if (ClassManager.hasClass(player)) {
            ClassAdvancementManager.tick(player);
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()
                && event.getOriginal() instanceof ServerPlayer oldPlayer
                && event.getEntity() instanceof ServerPlayer newPlayer) {
            event.getOriginal().reviveCaps();
            ClassManager.copyData(oldPlayer, newPlayer);
            event.getOriginal().invalidateCaps();
        }
    }

    @SubscribeEvent
    public static void onLogout(
        PlayerEvent.PlayerLoggedOutEvent event
    ) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ClassCreationManager.onLogout(player);
            ClassAdvancementManager.onLogout(player);
        }
    }
}
