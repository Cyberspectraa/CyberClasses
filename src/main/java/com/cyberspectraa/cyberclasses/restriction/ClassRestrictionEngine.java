package com.cyberspectraa.cyberclasses.restriction;

import com.cyberspectraa.cyberclasses.CyberClasses;
import com.cyberspectraa.cyberclasses.classdata.ArmorWeight;
import com.cyberspectraa.cyberclasses.classdata.ClassManager;
import com.cyberspectraa.cyberclasses.classdata.ClassRules;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.registries.ForgeRegistries;

public final class ClassRestrictionEngine {
    public static final TagKey<Item> SWORDS =
        tag("swords");
    public static final TagKey<Item> AXES =
        tag("axes");
    public static final TagKey<Item> RANGED_WEAPONS =
        tag("ranged_weapons");
    public static final TagKey<Item> MAGIC_ITEMS =
        tag("magic_items");
    public static final TagKey<Item> SHIELDS =
        tag("shields");
    public static final TagKey<Item> LIGHT_ARMOR =
        tag("light_armor");
    public static final TagKey<Item> MEDIUM_ARMOR =
        tag("medium_armor");
    public static final TagKey<Item> HEAVY_ARMOR =
        tag("heavy_armor");
    public static final TagKey<Item> UNRESTRICTED =
        tag("unrestricted");

    private ClassRestrictionEngine() {
    }

    public static RestrictionResult canUse(
        net.minecraft.server.level.ServerPlayer player,
        ItemStack stack
    ) {
        if (player == null
                || stack == null
                || stack.isEmpty()
                || stack.is(UNRESTRICTED)) {
            return RestrictionResult.permit();
        }

        ClassRules rules =
            ClassManager.getEffectiveRules(player);

        WeaponType type = weaponType(stack);

        return switch (type) {
            case SWORD -> rules.allowsSwords()
                ? RestrictionResult.permit()
                : RestrictionResult.denied("swords in combat");
            case AXE -> rules.allowsAxes()
                ? RestrictionResult.permit()
                : RestrictionResult.denied("axes in combat");
            case RANGED -> rules.allowsRanged()
                ? RestrictionResult.permit()
                : RestrictionResult.denied("ranged weapons");
            case MAGIC -> rules.allowsMagic()
                ? RestrictionResult.permit()
                : RestrictionResult.denied("magic");
            case SHIELD -> rules.allowsShields()
                ? RestrictionResult.permit()
                : RestrictionResult.denied("shields");
            case OTHER -> RestrictionResult.permit();
        };
    }

    public static RestrictionResult canUseOnBlock(
        net.minecraft.server.level.ServerPlayer player,
        ItemStack stack
    ) {
        if (stack == null || stack.isEmpty()) {
            return RestrictionResult.permit();
        }

        // Tool restrictions are combat restrictions, not survival
        // restrictions. Axes remain usable for logs/stripping regardless of
        // class, and ordinary swords/tools never block block interaction.
        if (stack.getItem() instanceof AxeItem
                || stack.getItem() instanceof SwordItem) {
            return RestrictionResult.permit();
        }

        WeaponType type = weaponType(stack);

        if (type == WeaponType.MAGIC
                || type == WeaponType.RANGED
                || type == WeaponType.SHIELD) {
            return canUse(player, stack);
        }

        return RestrictionResult.permit();
    }

    public static RestrictionResult canWear(
        net.minecraft.server.level.ServerPlayer player,
        ItemStack stack,
        EquipmentSlot slot
    ) {
        if (player == null
                || stack == null
                || stack.isEmpty()
                || !slot.isArmor()) {
            return RestrictionResult.permit();
        }

        ClassRules rules =
            ClassManager.getEffectiveRules(player);

        ArmorWeight weight = armorWeight(stack);

        if (rules.maxArmor().allows(weight)) {
            return RestrictionResult.permit();
        }

        return RestrictionResult.denied(
            weight.displayName().toLowerCase()
                + " armour"
        );
    }

    public static boolean mayCastMagic(
        net.minecraft.server.level.ServerPlayer player
    ) {
        return player == null
            || !ClassManager.hasClass(player)
            || ClassManager.getEffectiveRules(player)
                .allowsMagic();
    }

    public static ArmorWeight armorWeight(ItemStack stack) {
        if (stack.is(LIGHT_ARMOR)) {
            return ArmorWeight.LIGHT;
        }
        if (stack.is(MEDIUM_ARMOR)) {
            return ArmorWeight.MEDIUM;
        }
        if (stack.is(HEAVY_ARMOR)) {
            return ArmorWeight.HEAVY;
        }

        if (!(stack.getItem() instanceof ArmorItem armor)) {
            return ArmorWeight.NONE;
        }

        var material = armor.getMaterial();

        if (material == ArmorMaterials.LEATHER
                || material == ArmorMaterials.GOLD) {
            return ArmorWeight.LIGHT;
        }

        if (material == ArmorMaterials.CHAIN
                || material == ArmorMaterials.IRON
                || material == ArmorMaterials.TURTLE) {
            return ArmorWeight.MEDIUM;
        }

        if (material == ArmorMaterials.DIAMOND
                || material == ArmorMaterials.NETHERITE) {
            return ArmorWeight.HEAVY;
        }

        // Unknown modded armour defaults to medium until a datapack places it
        // in CyberClasses' light/medium/heavy tags.
        return ArmorWeight.MEDIUM;
    }

    private static WeaponType weaponType(ItemStack stack) {
        Item item = stack.getItem();

        if (stack.is(MAGIC_ITEMS) || looksMagical(stack)) {
            return WeaponType.MAGIC;
        }
        if (stack.is(RANGED_WEAPONS)
                || item instanceof BowItem
                || item instanceof CrossbowItem) {
            return WeaponType.RANGED;
        }
        if (stack.is(SHIELDS)
                || item instanceof ShieldItem) {
            return WeaponType.SHIELD;
        }
        if (stack.is(SWORDS)
                || item instanceof SwordItem
                || looksLikeSword(stack)) {
            return WeaponType.SWORD;
        }
        if (stack.is(AXES)
                || item instanceof AxeItem
                || looksLikeAxe(stack)) {
            return WeaponType.AXE;
        }

        return WeaponType.OTHER;
    }

    private static boolean looksMagical(ItemStack stack) {
        ResourceLocation id =
            ForgeRegistries.ITEMS.getKey(stack.getItem());

        if (id == null) {
            return false;
        }

        String path = id.getPath();

        return "irons_spellbooks".equals(id.getNamespace())
            && (path.contains("spell_book")
                || path.contains("spellbook")
                || path.contains("scroll")
                || path.contains("staff"));
    }

    private static boolean looksLikeSword(ItemStack stack) {
        ResourceLocation id =
            ForgeRegistries.ITEMS.getKey(stack.getItem());

        if (id == null) {
            return false;
        }

        String path = id.getPath();
        return path.endsWith("_sword")
            || path.contains("greatsword")
            || path.contains("longsword")
            || path.contains("rapier")
            || path.contains("katana")
            || path.contains("dagger");
    }

    private static boolean looksLikeAxe(ItemStack stack) {
        ResourceLocation id =
            ForgeRegistries.ITEMS.getKey(stack.getItem());

        if (id == null) {
            return false;
        }

        String path = id.getPath();

        if (path.contains("pickaxe")) {
            return false;
        }

        return path.endsWith("_axe")
            || path.contains("battleaxe")
            || path.contains("battle_axe")
            || path.contains("greataxe")
            || path.contains("great_axe");
    }

    private static TagKey<Item> tag(String path) {
        return TagKey.create(
            Registries.ITEM,
            new ResourceLocation(CyberClasses.MOD_ID, path)
        );
    }

    private enum WeaponType {
        SWORD,
        AXE,
        RANGED,
        MAGIC,
        SHIELD,
        OTHER
    }

    public record RestrictionResult(
        boolean allowed,
        String reason
    ) {
        public static RestrictionResult permit() {
            return new RestrictionResult(true, "");
        }

        public static RestrictionResult denied(String reason) {
            return new RestrictionResult(false, reason);
        }
    }
}
