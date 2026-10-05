package com.cyberspectraa.cyberclasses.classdata;

import net.minecraft.util.RandomSource;

/**
 * Stable, reflection-friendly bridge for mods such as CyberNpc.
 * Class definitions and NPC spawn metadata live here in CyberClasses; callers
 * only need to store the returned class id and implement their own behaviour.
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
        return CyberClass.byId(id)
            .orElse(CyberClass.CLASSLESS)
            .usesSpellBook();
    }

    public static boolean hasMagicSchool(String id) {
        return CyberClass.byId(id)
            .orElse(CyberClass.CLASSLESS)
            .hasMagicSchool();
    }
}
