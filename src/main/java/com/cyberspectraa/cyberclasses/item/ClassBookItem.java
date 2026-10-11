package com.cyberspectraa.cyberclasses.item;

import com.cyberspectraa.cyberclasses.classdata.ClassAdvancement;
import com.cyberspectraa.cyberclasses.classdata.ClassManager;
import com.cyberspectraa.cyberclasses.classdata.CyberClass;
import com.cyberspectraa.cyberclasses.classdata.CyberProgressionBridge;
import com.cyberspectraa.cyberclasses.compat.PhotonClassChangeEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Four persistent, consumable class tomes. A particular book always contains
 * its target path in signed-by-server item NBT; the client NEVER supplies a
 * requested class. First use of an unattuned creative/give book attunes it,
 * second use applies the change after level and class checks.
 *
 * Common/uncommon: base-class changes. Rare: upgrade the current base class
 * along a compatible advancement. Legendary: change to any advanced path.
 */
public final class ClassBookItem extends Item {
    private static final String TARGET_CLASS = "CyberClassBookTarget";
    private static final String TARGET_ADV = "CyberClassBookAdvancement";

    public enum Tier {
        COMMON(8, false, ChatFormatting.GRAY),
        UNCOMMON(20, false, ChatFormatting.GREEN),
        RARE(30, true, ChatFormatting.AQUA),
        LEGENDARY(50, true, ChatFormatting.LIGHT_PURPLE);

        private final int level;
        private final boolean advanced;
        private final ChatFormatting color;
        Tier(int level, boolean advanced, ChatFormatting color) {
            this.level = level;
            this.advanced = advanced;
            this.color = color;
        }
        public int requiredLevel() { return level; }
        public boolean advanced() { return advanced; }
    }

    private final Tier tier;
    public ClassBookItem(Tier tier, Properties properties) {
        super(properties.stacksTo(1));
        this.tier = tier;
    }

    public Tier tier() { return tier; }

    /** Used by loot events and when an operator gives an unattuned book. */
    public static ItemStack attune(ItemStack stack, @Nullable ServerPlayer owner, RandomSource random) {
        if (!(stack.getItem() instanceof ClassBookItem item) || random == null) return stack;
        if (stack.hasTag() && stack.getTag().contains(TARGET_CLASS)) return stack;

        CyberClass chosen;
        ClassAdvancement advancement = null;
        if (item.tier == Tier.COMMON) {
            CyberClass[] basic = {
                CyberClass.KNIGHT, CyberClass.ARCHER, CyberClass.ROGUE,
                CyberClass.RANGER, CyberClass.MONK, CyberClass.ALCHEMIST
            };
            chosen = basic[random.nextInt(basic.length)];
        } else if (item.tier == Tier.UNCOMMON) {
            CyberClass[] classes = CyberClass.playerChoices();
            chosen = classes[random.nextInt(classes.length)];
        } else if (item.tier == Tier.RARE) {
            chosen = owner == null ? CyberClass.KNIGHT
                    : ClassManager.getClass(owner).orElse(CyberClass.KNIGHT);
            ClassAdvancement[] paths = ClassAdvancement.choicesFor(chosen);
            if (paths.length > 0) advancement = paths[random.nextInt(paths.length)];
        } else {
            CyberClass[] classes = CyberClass.playerChoices();
            chosen = classes[random.nextInt(classes.length)];
            ClassAdvancement[] paths = ClassAdvancement.choicesFor(chosen);
            if (paths.length > 0) advancement = paths[random.nextInt(paths.length)];
        }
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(TARGET_CLASS, chosen.id());
        if (advancement != null) tag.putString(TARGET_ADV, advancement.id());
        return stack;
    }

    private CyberClass targetClass(ItemStack stack) {
        if (!stack.hasTag()) return null;
        return CyberClass.playerById(stack.getTag().getString(TARGET_CLASS)).orElse(null);
    }

    private ClassAdvancement targetAdvancement(ItemStack stack) {
        if (!stack.hasTag()) return null;
        return ClassAdvancement.byId(stack.getTag().getString(TARGET_ADV)).orElse(null);
    }

    @Override
    public Component getName(ItemStack stack) {
        CyberClass base = targetClass(stack);
        ClassAdvancement advanced = targetAdvancement(stack);
        String target = advanced != null ? advanced.displayName()
                : base != null ? base.displayName() : "Unattuned";
        return Component.literal(tier.name().charAt(0)
                + tier.name().substring(1).toLowerCase() + " Class Book: " + target)
                .withStyle(tier.color);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
            List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Requires Cyber Level " + tier.requiredLevel())
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(tier.advanced
                ? tier == Tier.RARE ? "Advances your current class"
                    : "Can transform you into another advanced class"
                : "Changes your base class and clears its previous advancement")
                .withStyle(ChatFormatting.DARK_GRAY));
        if (targetClass(stack) == null) tooltip.add(
                Component.literal("Right-click to discover the class in this book.")
                        .withStyle(ChatFormatting.YELLOW));
        else if (tier == Tier.RARE) tooltip.add(
                Component.literal("Only usable by a matching base class.")
                        .withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return tier.advanced;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        if (level.isClientSide) return InteractionResultHolder.success(stack);
        if (!(user instanceof ServerPlayer player)) return InteractionResultHolder.fail(stack);
        if (!ClassManager.hasClass(player)) {
            player.displayClientMessage(Component.literal("Complete character creation first.")
                    .withStyle(ChatFormatting.RED), true);
            return InteractionResultHolder.fail(stack);
        }

        if (targetClass(stack) == null) {
            attune(stack, player, player.getRandom());
            player.displayClientMessage(Component.literal("The book reveals: ")
                    .append(getName(stack)), false);
            return InteractionResultHolder.success(stack);
        }

        int levelRequired = Math.max(tier.requiredLevel(),
                tier.advanced ? ClassAdvancement.REQUIRED_LEVEL : 1);
        if (CyberProgressionBridge.getLevel(player) < levelRequired) {
            player.displayClientMessage(Component.literal(
                    "Requires Cyber Level " + levelRequired + ".")
                    .withStyle(ChatFormatting.RED), true);
            return InteractionResultHolder.fail(stack);
        }

        CyberClass chosen = targetClass(stack);
        ClassAdvancement advancement = targetAdvancement(stack);
        if (tier.advanced && (advancement == null || advancement.baseClass() != chosen)) {
            return InteractionResultHolder.fail(stack);
        }
        CyberClass before = ClassManager.getClass(player).orElse(null);
        ClassAdvancement oldAdvancement = ClassManager.getAdvancement(player).orElse(null);
        if (tier == Tier.RARE && before != chosen) {
            player.displayClientMessage(Component.literal(
                    "This rare book requires the " + chosen.displayName() + " base class.")
                    .withStyle(ChatFormatting.RED), true);
            return InteractionResultHolder.fail(stack);
        }
        if (tier.advanced && oldAdvancement == advancement) {
            player.displayClientMessage(Component.literal(
                    "You have already mastered this advancement."), true);
            return InteractionResultHolder.fail(stack);
        }
        if (!tier.advanced && before == chosen && oldAdvancement == null) {
            player.displayClientMessage(Component.literal(
                    "You already belong to this base class."), true);
            return InteractionResultHolder.fail(stack);
        }

        // Changing the base class invalidates an old, incompatible branch.
        if (before != chosen || !tier.advanced) ClassManager.setClass(player, chosen);
        if (tier.advanced && !ClassManager.setAdvancement(player, advancement)) {
            // All preconditions were verified above; do not consume on failure.
            return InteractionResultHolder.fail(stack);
        }

        player.sendSystemMessage(Component.literal("Class transformed: ")
                .withStyle(ChatFormatting.GOLD)
                .append(Component.literal(tier.advanced
                        ? advancement.displayName() : chosen.displayName())
                        .withStyle(tier.color)));
        // Play the optional Photon ceremony only after a valid, successful
        // class change. Invalid or insufficient-level books produce no VFX.
        PhotonClassChangeEffects.play(player, tier);
        if (!player.getAbilities().instabuild) stack.shrink(1);
        return InteractionResultHolder.success(stack);
    }
}
