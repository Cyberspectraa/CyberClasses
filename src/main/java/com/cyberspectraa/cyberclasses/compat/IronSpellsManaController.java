package com.cyberspectraa.cyberclasses.compat;

import com.cyberspectraa.cyberclasses.classdata.ClassManager;
import com.cyberspectraa.cyberclasses.classdata.ClassRules;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.UUID;

public final class IronSpellsManaController {
    private static final String MOD_ID = "irons_spellbooks";

    private static final ResourceLocation MAX_MANA =
        new ResourceLocation(MOD_ID, "max_mana");
    private static final ResourceLocation MANA_REGEN =
        new ResourceLocation(MOD_ID, "mana_regen");

    private static final UUID MAX_MANA_CLASS_ID =
        UUID.fromString("0dd2fa9b-9371-4f5b-a1bc-d9c275b6ea11");
    private static final UUID MANA_REGEN_CLASS_ID =
        UUID.fromString("330b15d1-3cde-4f03-948c-f8ae8f5966d2");

    private static boolean reflectionResolved;
    private static Method getPlayerMagicData;
    private static Method getMana;
    private static Method setMana;

    private IronSpellsManaController() {
    }

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }

    public static void apply(ServerPlayer player) {
        if (!isLoaded() || player == null) {
            return;
        }

        if (!ClassManager.hasClass(player)) {
            clear(player);
            return;
        }

        ClassRules rules =
            ClassManager.getEffectiveRules(player);

        double maxMultiplier = rules.hasMana()
            ? rules.manaTier().maxManaMultiplier()
            : 0.0D;

        double regenMultiplier = rules.hasMana()
            ? rules.manaTier().manaRegenMultiplier()
            : 0.0D;

        applyMultiplier(
            player,
            MAX_MANA,
            MAX_MANA_CLASS_ID,
            "CyberClasses class max mana",
            maxMultiplier
        );

        applyMultiplier(
            player,
            MANA_REGEN,
            MANA_REGEN_CLASS_ID,
            "CyberClasses class mana regeneration",
            regenMultiplier
        );

        clampStoredMana(player, rules.hasMana());
    }

    public static void clear(ServerPlayer player) {
        if (!isLoaded() || player == null) {
            return;
        }

        remove(
            player,
            MAX_MANA,
            MAX_MANA_CLASS_ID
        );
        remove(
            player,
            MANA_REGEN,
            MANA_REGEN_CLASS_ID
        );
    }

    private static void applyMultiplier(
        ServerPlayer player,
        ResourceLocation attributeId,
        UUID modifierId,
        String name,
        double multiplier
    ) {
        Attribute attribute =
            ForgeRegistries.ATTRIBUTES.getValue(attributeId);

        if (attribute == null) {
            return;
        }

        AttributeInstance instance =
            player.getAttribute(attribute);

        if (instance == null) {
            return;
        }

        instance.removeModifier(modifierId);

        double amount = multiplier - 1.0D;

        if (Math.abs(amount) < 0.000001D) {
            return;
        }

        instance.addTransientModifier(
            new AttributeModifier(
                modifierId,
                name,
                amount,
                AttributeModifier.Operation.MULTIPLY_TOTAL
            )
        );
    }

    private static void remove(
        ServerPlayer player,
        ResourceLocation attributeId,
        UUID modifierId
    ) {
        Attribute attribute =
            ForgeRegistries.ATTRIBUTES.getValue(attributeId);

        if (attribute == null) {
            return;
        }

        AttributeInstance instance =
            player.getAttribute(attribute);

        if (instance != null) {
            instance.removeModifier(modifierId);
        }
    }

    private static void clampStoredMana(
        ServerPlayer player,
        boolean hasMana
    ) {
        resolveReflection();

        if (getPlayerMagicData == null
                || getMana == null
                || setMana == null) {
            return;
        }

        try {
            Object magicData =
                getPlayerMagicData.invoke(null, player);

            if (magicData == null) {
                return;
            }

            Object rawMana = getMana.invoke(magicData);
            double currentMana =
                rawMana instanceof Number number
                    ? number.doubleValue()
                    : 0.0D;

            Attribute maxManaAttribute =
                ForgeRegistries.ATTRIBUTES.getValue(MAX_MANA);

            double maximum = maxManaAttribute == null
                ? 0.0D
                : Math.max(
                    0.0D,
                    player.getAttributeValue(maxManaAttribute)
                );

            double desired = hasMana
                ? Math.min(currentMana, maximum)
                : 0.0D;

            if (Math.abs(currentMana - desired) > 0.001D) {
                invokeSetMana(magicData, desired);
            }
        } catch (ReflectiveOperationException | RuntimeException ignored) {
        }
    }

    private static void resolveReflection() {
        if (reflectionResolved) {
            return;
        }

        reflectionResolved = true;

        try {
            Class<?> magicDataClass = Class.forName(
                "io.redspace.ironsspellbooks.api.magic.MagicData"
            );

            for (Method method : magicDataClass.getMethods()) {
                if (method.getName().equals("getPlayerMagicData")
                        && Modifier.isStatic(method.getModifiers())
                        && method.getParameterCount() == 1) {
                    getPlayerMagicData = method;
                    break;
                }
            }

            getMana = magicDataClass.getMethod("getMana");

            for (Method method : magicDataClass.getMethods()) {
                if (method.getName().equals("setMana")
                        && method.getParameterCount() == 1) {
                    setMana = method;
                    break;
                }
            }
        } catch (ReflectiveOperationException | LinkageError ignored) {
            getPlayerMagicData = null;
            getMana = null;
            setMana = null;
        }
    }

    private static void invokeSetMana(
        Object magicData,
        double value
    ) throws ReflectiveOperationException {
        Class<?> parameter =
            setMana.getParameterTypes()[0];

        Object converted;

        if (parameter == float.class
                || parameter == Float.class) {
            converted = (float) value;
        } else if (parameter == int.class
                || parameter == Integer.class) {
            converted = (int) Math.round(value);
        } else if (parameter == long.class
                || parameter == Long.class) {
            converted = Math.round(value);
        } else {
            converted = value;
        }

        setMana.invoke(magicData, converted);
    }
}
