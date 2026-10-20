package com.cyberspectraa.cyberclasses.classdata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * One optional, modest guild starter kit per character. Class selection is
 * mandatory; taking supplies is not. The decision is saved BEFORE delivering
 * items, and stays saved even across admin class resets, deaths and logins.
 */
public final class StarterKitService {
    private static final String DECIDED = "StarterKitDecided";
    private static final String ACCEPTED = "StarterKitAccepted";

    private StarterKitService() {}

    public static void complete(ServerPlayer player, CyberClass chosen, boolean accept) {
        CompoundTag data = player.getPersistentData();
        CompoundTag classes = data.getCompound(ClassManager.ROOT_KEY);
        if (classes.getBoolean(DECIDED)) return;
        classes.putBoolean(DECIDED, true);
        classes.putBoolean(ACCEPTED, accept);
        data.put(ClassManager.ROOT_KEY, classes);

        if (!accept) {
            player.sendSystemMessage(Component.literal(
                    "You declined the guild starter equipment."));
            return;
        }

        List<ItemStack> kit = build(chosen);
        for (ItemStack stack : kit) {
            if (!player.getInventory().add(stack) && !stack.isEmpty()) {
                player.drop(stack, false);
            }
        }
        player.getInventory().setChanged();
        player.sendSystemMessage(Component.literal(
                "Guild starter equipment issued. Good luck out there."));
    }

    private static List<ItemStack> build(CyberClass type) {
        List<ItemStack> kit = new ArrayList<>();
        switch (type) {
            case KNIGHT -> {
                put(kit, Items.STONE_SWORD);
                put(kit, Items.SHIELD);
                put(kit, Items.LEATHER_CHESTPLATE);
                put(kit, Items.LEATHER_LEGGINGS);
            }
            case BERSERKER -> {
                put(kit, Items.STONE_AXE);
                put(kit, Items.LEATHER_CHESTPLATE);
            }
            case ARCHER -> {
                put(kit, Items.BOW);
                put(kit, Items.ARROW, 24);
                put(kit, Items.WOODEN_SWORD);
                put(kit, Items.LEATHER_CHESTPLATE);
            }
            case RANGER -> {
                put(kit, Items.BOW);
                put(kit, Items.ARROW, 16);
                put(kit, Items.STONE_SWORD);
                put(kit, Items.LEATHER_CHESTPLATE);
            }
            case ROGUE -> {
                put(kit, Items.STONE_SWORD);
                put(kit, Items.LEATHER_CHESTPLATE);
                put(kit, Items.LEATHER_BOOTS);
            }
            case MONK -> {
                put(kit, Items.LEATHER_CHESTPLATE);
                put(kit, Items.LEATHER_BOOTS);
            }
            case MAGE -> {
                // A book with NO spells. The Mage learns spells in the world.
                // Their only allowed sword is this humble wooden one.
                spellbook(kit);
                put(kit, Items.WOODEN_SWORD);
                put(kit, Items.LEATHER_CHESTPLATE);
            }
            case CLERIC -> {
                put(kit, Items.STONE_AXE);
                put(kit, Items.SHIELD);
                spellbook(kit);
                put(kit, Items.LEATHER_CHESTPLATE);
            }
            case SPELLBLADE -> {
                put(kit, Items.STONE_SWORD);
                spellbook(kit);
                put(kit, Items.LEATHER_CHESTPLATE);
            }
            case DRUID -> {
                spellbook(kit);
                put(kit, Items.WOODEN_AXE);
                put(kit, Items.LEATHER_CHESTPLATE);
            }
            case BARD -> {
                put(kit, Items.WOODEN_SWORD);
                spellbook(kit);
                put(kit, Items.LEATHER_CHESTPLATE);
            }
            case ALCHEMIST -> {
                put(kit, Items.GLASS_BOTTLE, 5);
                put(kit, Items.POTION, 2);
                put(kit, Items.LEATHER_CHESTPLATE);
            }
            case CLASSLESS -> { return kit; }
        }
        put(kit, Items.BREAD, 6);
        return kit;
    }

    private static void spellbook(List<ItemStack> kit) {
        if (ModList.get().isLoaded("irons_spellbooks")) {
            Item iron = ForgeRegistries.ITEMS.getValue(
                    new ResourceLocation("irons_spellbooks", "copper_spell_book"));
            if (iron != null && iron != Items.AIR) {
                // Fresh item has no SpellContainer / learned spells.
                put(kit, iron);
                return;
            }
        }
        // Fallback only when the optional mod is absent.
        put(kit, Items.BOOK);
    }

    private static void put(List<ItemStack> kit, Item item) {
        put(kit, item, 1);
    }

    private static void put(List<ItemStack> kit, Item item, int count) {
        kit.add(new ItemStack(item, count));
    }
}
