package com.cyberspectraa.cyberclasses.registry;

import com.cyberspectraa.cyberclasses.CyberClasses;
import com.cyberspectraa.cyberclasses.item.ClassBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CyberClasses.MOD_ID);

    public static final RegistryObject<Item> COMMON_CLASS_BOOK =
            ITEMS.register("common_class_book", () -> new ClassBookItem(
                    ClassBookItem.Tier.COMMON, new Item.Properties().rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> UNCOMMON_CLASS_BOOK =
            ITEMS.register("uncommon_class_book", () -> new ClassBookItem(
                    ClassBookItem.Tier.UNCOMMON, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> RARE_CLASS_BOOK =
            ITEMS.register("rare_class_book", () -> new ClassBookItem(
                    ClassBookItem.Tier.RARE, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> LEGENDARY_CLASS_BOOK =
            ITEMS.register("legendary_class_book", () -> new ClassBookItem(
                    ClassBookItem.Tier.LEGENDARY, new Item.Properties().rarity(Rarity.EPIC)));

    private ModItems() {}
}
