package com.cyberspectraa.cyberclasses.event;

import com.cyberspectraa.cyberclasses.classdata.ClassManager;
import com.cyberspectraa.cyberclasses.restriction.ClassRestrictionEngine;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ClassRestrictionEvents {
    private static final Map<UUID, Long> MESSAGE_COOLDOWN =
        new HashMap<>();

    private ClassRestrictionEvents() {
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || !ClassManager.hasClass(player)) {
            return;
        }

        var result = ClassRestrictionEngine.canUse(
            player,
            player.getMainHandItem()
        );

        if (!result.allowed()) {
            event.setCanceled(true);
            deny(player, result.reason());
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(
        PlayerInteractEvent.RightClickItem event
    ) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        var result = ClassRestrictionEngine.canUse(
            player,
            event.getItemStack()
        );

        if (!result.allowed()) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
            deny(player, result.reason());
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(
        PlayerInteractEvent.RightClickBlock event
    ) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        var result = ClassRestrictionEngine.canUse(
            player,
            event.getItemStack()
        );

        if (!result.allowed()) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
            deny(player, result.reason());
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(
        PlayerInteractEvent.EntityInteract event
    ) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        var result = ClassRestrictionEngine.canUse(
            player,
            event.getItemStack()
        );

        if (!result.allowed()) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
            deny(player, result.reason());
        }
    }

    @SubscribeEvent
    public static void onArrowLoose(ArrowLooseEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        var result = ClassRestrictionEngine.canUse(
            player,
            player.getUseItem()
        );

        if (!result.allowed()) {
            event.setCanceled(true);
            deny(player, result.reason());
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(
        TickEvent.PlayerTickEvent event
    ) {
        if (event.phase != TickEvent.Phase.END
                || !(event.player instanceof ServerPlayer player)
                || !ClassManager.hasClass(player)
                || player.tickCount % 5 != 0) {
            return;
        }

        enforceArmor(player);
    }

    private static void enforceArmor(ServerPlayer player) {
        EquipmentSlot[] slots = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
        };

        for (EquipmentSlot slot : slots) {
            ItemStack stack = player.getItemBySlot(slot);

            if (stack.isEmpty()) {
                continue;
            }

            var result =
                ClassRestrictionEngine.canWear(
                    player,
                    stack,
                    slot
                );

            if (result.allowed()) {
                continue;
            }

            ItemStack removed = stack.copy();
            player.setItemSlot(slot, ItemStack.EMPTY);

            if (!player.getInventory().add(removed)) {
                player.drop(removed, false);
            }

            deny(player, result.reason());
        }
    }

    public static void deny(
        ServerPlayer player,
        String reason
    ) {
        long now = player.level().getGameTime();
        long readyAt = MESSAGE_COOLDOWN.getOrDefault(
            player.getUUID(),
            0L
        );

        if (now < readyAt) {
            return;
        }

        MESSAGE_COOLDOWN.put(
            player.getUUID(),
            now + 30L
        );

        String className = ClassManager.getClass(player)
            .map(value -> value.displayName())
            .orElse("Current class");

        player.displayClientMessage(
            Component.literal(
                className + " cannot use " + reason + "."
            ).withStyle(ChatFormatting.RED),
            true
        );
    }
}
