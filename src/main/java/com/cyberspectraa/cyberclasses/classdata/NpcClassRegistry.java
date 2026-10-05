package com.cyberspectraa.cyberclasses.classdata;

import net.minecraft.util.RandomSource;

/**
 * Stable, reflection-friendly bridge for CyberNpc.
 * CyberClasses owns class/advancement definitions and rules; CyberNpc owns
 * behaviour decisions, equipment execution and combat AI.
 */
public final class NpcClassRegistry {
    private NpcClassRegistry() {
    }

    public static String normalize(String id) {
        return CyberClass.byId(id)
            .orElse(CyberClass.CLASSLESS)
            .id();
    }

    public static String displayName(String id) {
        return CyberClass.byId(id)
            .orElse(CyberClass.CLASSLESS)
            .displayName();
    }

    public static String randomClassId(RandomSource random) {
        return CyberClass.randomNpcClass(random).id();
    }

    public static boolean isAvailable(String id) {
        return CyberClass.byId(id)
            .orElse(CyberClass.CLASSLESS)
            .isAvailableForNpc();
    }

    public static boolean usesSpellBook(String id) {
        return effectiveRules(id, "").allowsMagic();
    }

    public static boolean usesSpellBook(
        String classId,
        String advancementId
    ) {
        return effectiveRules(
            classId,
            advancementId
        ).allowsMagic();
    }

    public static boolean hasMagicSchool(String id) {
        return usesSpellBook(id);
    }

    public static boolean hasMagicSchool(
        String classId,
        String advancementId
    ) {
        return usesSpellBook(
            classId,
            advancementId
        );
    }

    public static String randomAdvancementId(
        String classId,
        int level,
        RandomSource random
    ) {
        if (level < ClassAdvancement.REQUIRED_LEVEL) {
            return "";
        }

        CyberClass baseClass = CyberClass.byId(classId)
            .orElse(CyberClass.CLASSLESS);

        return ClassAdvancement.randomFor(
            baseClass,
            random
        ).map(ClassAdvancement::id)
            .orElse("");
    }

    public static String normalizeAdvancement(
        String classId,
        String advancementId
    ) {
        CyberClass baseClass = CyberClass.byId(classId)
            .orElse(CyberClass.CLASSLESS);

        ClassAdvancement advancement =
            ClassAdvancement.byId(
                advancementId
            ).orElse(null);

        if (advancement == null
                || advancement.baseClass() != baseClass) {
            return "";
        }

        return advancement.id();
    }

    public static String advancementDisplayName(
        String advancementId
    ) {
        return ClassAdvancement.byId(advancementId)
            .map(ClassAdvancement::displayName)
            .orElse("");
    }

    public static String effectiveDisplayName(
        String classId,
        String advancementId
    ) {
        String normalized = normalizeAdvancement(
            classId,
            advancementId
        );

        if (!normalized.isBlank()) {
            return advancementDisplayName(normalized);
        }

        return displayName(classId);
    }

    public static String manaTier(
        String classId,
        String advancementId
    ) {
        return effectiveRules(
            classId,
            advancementId
        ).manaTier().name();
    }

    public static String maxArmor(
        String classId,
        String advancementId
    ) {
        return effectiveRules(
            classId,
            advancementId
        ).maxArmor().name();
    }

    private static ClassRules effectiveRules(
        String classId,
        String advancementId
    ) {
        CyberClass baseClass = CyberClass.byId(classId)
            .orElse(CyberClass.CLASSLESS);

        ClassAdvancement advancement =
            ClassAdvancement.byId(
                advancementId
            ).orElse(null);

        if (advancement != null
                && advancement.baseClass() == baseClass) {
            return advancement.rules();
        }

        return baseClass.rules();
    }
}
