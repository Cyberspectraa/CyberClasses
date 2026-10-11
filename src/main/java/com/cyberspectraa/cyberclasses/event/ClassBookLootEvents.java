package com.cyberspectraa.cyberclasses.event;

import com.cyberspectraa.cyberclasses.item.ClassBookItem;
import com.cyberspectraa.cyberclasses.registry.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Survival acquisition: hostile-mob kills have a small chance to yield a
 * persistent, attuned Class Book. Boss kills guarantee a higher-tier tome.
 * It is awarded once as ordinary item loot, not to the inventory, so pickup
 * and trading use vanilla mechanics. No separate loot-table overrides.
 */
public final class ClassBookLootEvents {
    private ClassBookLootEvents() {}

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide
                || !(event.getSource().getEntity() instanceof ServerPlayer player)
                || !(event.getEntity() instanceof Enemy
                    || event.getEntity() instanceof EnderDragon)) {
            return;
        }
        boolean boss = event.getEntity() instanceof WitherBoss
                || event.getEntity() instanceof EnderDragon;
        if (!boss && player.getRandom().nextFloat() >= 0.026F) return;

        float roll = player.getRandom().nextFloat();
        ItemStack book;
        if (boss) {
            book = new ItemStack(roll < 0.22F
                    ? ModItems.LEGENDARY_CLASS_BOOK.get()
                    : ModItems.RARE_CLASS_BOOK.get());
        } else if (roll < 0.67F) {
            book = new ItemStack(ModItems.COMMON_CLASS_BOOK.get());
        } else if (roll < 0.91F) {
            book = new ItemStack(ModItems.UNCOMMON_CLASS_BOOK.get());
        } else if (roll < 0.995F) {
            book = new ItemStack(ModItems.RARE_CLASS_BOOK.get());
        } else {
            book = new ItemStack(ModItems.LEGENDARY_CLASS_BOOK.get());
        }

        ClassBookItem.attune(book, player, player.getRandom());
        event.getDrops().add(new ItemEntity(
                event.getEntity().level(),
                event.getEntity().getX(),
                event.getEntity().getY() + 0.3D,
                event.getEntity().getZ(),
                book));
    }
}
