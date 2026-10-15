package com.cyberspectraa.cyberclasses.classdata;

import net.minecraft.util.RandomSource;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum ClassAdvancement {
    VANGUARD(
        "vanguard", "Vanguard",
        "Heavy armour, sword or axe, and a shield.",
        CyberClass.KNIGHT,
        true, true, false, false, true,
        ArmorWeight.HEAVY, ManaTier.NONE
    ),
    TEMPLAR(
        "templar", "Templar",
        "Steel and shield in heavy armour, without spells.",
        CyberClass.KNIGHT,
        true, true, false, false, true,
        ArmorWeight.HEAVY, ManaTier.NONE
    ),
    GUARDIAN(
        "guardian", "Guardian",
        "A shield-bearing knight in heavy armour.",
        CyberClass.KNIGHT,
        true, false, false, false, true,
        ArmorWeight.HEAVY, ManaTier.NONE
    ),
    REAVER(
        "reaver", "Reaver",
        "Heavy armour and a sword or axe. No shield.",
        CyberClass.BERSERKER,
        true, true, false, false, false,
        ArmorWeight.HEAVY, ManaTier.NONE
    ),
    JUGGERNAUT(
        "juggernaut", "Juggernaut",
        "A heavy-armoured fighter using swords and axes.",
        CyberClass.BERSERKER,
        true, true, false, false, false,
        ArmorWeight.HEAVY, ManaTier.NONE
    ),
    BLOODRAGER(
        "bloodrager", "Bloodrager",
        "Heavy melee weapons, without a shield or magic.",
        CyberClass.BERSERKER,
        true, true, false, false, false,
        ArmorWeight.HEAVY, ManaTier.NONE
    ),
    MARKSMAN(
        "marksman", "Marksman",
        "Ranged weapons and a sword, with medium armour.",
        CyberClass.ARCHER,
        true, false, true, false, false,
        ArmorWeight.MEDIUM, ManaTier.NONE
    ),
    SHARPSHOOTER(
        "sharpshooter", "Sharpshooter",
        "Bow first, sword as a backup; medium armour.",
        CyberClass.ARCHER,
        true, false, true, false, false,
        ArmorWeight.MEDIUM, ManaTier.NONE
    ),
    ARCANE_ARCHER(
        "arcane_archer", "Arcane Archer",
        "Bows and spells, with a modest mana pool.",
        CyberClass.ARCHER,
        true, false, true, true, false,
        ArmorWeight.MEDIUM, ManaTier.LOW_MEDIUM
    ),
    HUNTER(
        "hunter", "Hunter",
        "Hunts with bows, swords and axes; medium armour.",
        CyberClass.RANGER,
        true, true, true, false, false,
        ArmorWeight.MEDIUM, ManaTier.NONE
    ),
    PATHFINDER(
        "pathfinder", "Pathfinder",
        "Travels with ranged and melee weapons.",
        CyberClass.RANGER,
        true, true, true, false, false,
        ArmorWeight.MEDIUM, ManaTier.NONE
    ),
    WARDEN(
        "warden", "Warden",
        "A wilderness fighter with swords, axes and bows.",
        CyberClass.RANGER,
        true, true, true, false, false,
        ArmorWeight.MEDIUM, ManaTier.NONE
    ),
    ASSASSIN(
        "assassin", "Assassin",
        "A light-armoured swordsman without a shield.",
        CyberClass.ROGUE,
        true, false, false, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE
    ),
    DUELIST(
        "duelist", "Duelist",
        "A swordsman in medium armour; no spells or shield.",
        CyberClass.ROGUE,
        true, false, false, false, false,
        ArmorWeight.MEDIUM, ManaTier.NONE
    ),
    SHADOW(
        "shadow", "Shadow",
        "Light armour, a sword and no shield.",
        CyberClass.ROGUE,
        true, false, false, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE
    ),
    IRON_FIST(
        "iron_fist", "Iron Fist",
        "A fighter in light armour, without weapons or spells.",
        CyberClass.MONK,
        false, false, false, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE
    ),
    WINDWALKER(
        "windwalker", "Windwalker",
        "Light armour and unarmed fighting.",
        CyberClass.MONK,
        false, false, false, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE
    ),
    ASCETIC(
        "ascetic", "Ascetic",
        "A monk who goes without swords, bows or magic.",
        CyberClass.MONK,
        false, false, false, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE
    ),
    SORCERER(
        "sorcerer", "Sorcerer",
        "A caster with very high mana and light armour.",
        CyberClass.MAGE,
        false, false, false, true, false,
        ArmorWeight.LIGHT, ManaTier.VERY_HIGH
    ),
    ELEMENTALIST(
        "elementalist", "Elementalist",
        "High mana for casting, with only light armour.",
        CyberClass.MAGE,
        false, false, false, true, false,
        ArmorWeight.LIGHT, ManaTier.HIGH
    ),
    NECROMANCER(
        "necromancer", "Necromancer",
        "Spells instead of weapons, with light armour.",
        CyberClass.MAGE,
        false, false, false, true, false,
        ArmorWeight.LIGHT, ManaTier.HIGH
    ),
    PALADIN(
        "paladin", "Paladin",
        "Heavy armour, swords, axes, shields and a little magic.",
        CyberClass.CLERIC,
        true, true, false, true, true,
        ArmorWeight.HEAVY, ManaTier.LOW_MEDIUM
    ),
    ORACLE(
        "oracle", "Oracle",
        "Spells and a shield, with medium armour.",
        CyberClass.CLERIC,
        false, false, false, true, true,
        ArmorWeight.MEDIUM, ManaTier.MEDIUM
    ),
    INQUISITOR(
        "inquisitor", "Inquisitor",
        "Axe, shield and spells, with medium armour.",
        CyberClass.CLERIC,
        false, true, false, true, true,
        ArmorWeight.MEDIUM, ManaTier.MEDIUM
    ),
    BATTLEMAGE(
        "battlemage", "Battlemage",
        "Heavy armour, a sword and a medium mana pool.",
        CyberClass.SPELLBLADE,
        true, false, false, true, false,
        ArmorWeight.HEAVY, ManaTier.MEDIUM
    ),
    RUNE_KNIGHT(
        "rune_knight", "Rune Knight",
        "Swords, axes, shields and magic in heavy armour.",
        CyberClass.SPELLBLADE,
        true, true, false, true, true,
        ArmorWeight.HEAVY, ManaTier.LOW_MEDIUM
    ),
    ARCANE_DUELIST(
        "arcane_duelist", "Arcane Duelist",
        "Sword and spells in medium armour; limited mana.",
        CyberClass.SPELLBLADE,
        true, false, false, true, false,
        ArmorWeight.MEDIUM, ManaTier.LOW_MEDIUM
    ),
    SHAMAN(
        "shaman", "Shaman",
        "High-mana caster wearing medium armour.",
        CyberClass.DRUID,
        false, false, false, true, false,
        ArmorWeight.MEDIUM, ManaTier.HIGH
    ),
    WILDSPEAKER(
        "wildspeaker", "Wildspeaker",
        "A druid who fights with spells instead of blades.",
        CyberClass.DRUID,
        false, false, false, true, false,
        ArmorWeight.MEDIUM, ManaTier.HIGH
    ),
    STORMCALLER(
        "stormcaller", "Stormcaller",
        "High-mana spellcaster with medium armour.",
        CyberClass.DRUID,
        false, false, false, true, false,
        ArmorWeight.MEDIUM, ManaTier.HIGH
    ),
    SKALD(
        "skald", "Skald",
        "A sword-carrying bard in medium armour.",
        CyberClass.BARD,
        true, false, false, true, false,
        ArmorWeight.MEDIUM, ManaTier.MEDIUM
    ),
    MINSTREL(
        "minstrel", "Minstrel",
        "Spells and light armour; no standard weapons.",
        CyberClass.BARD,
        false, false, false, true, false,
        ArmorWeight.LIGHT, ManaTier.MEDIUM
    ),
    MESMER(
        "mesmer", "Mesmer",
        "A light-armoured caster without shield or bow.",
        CyberClass.BARD,
        false, false, false, true, false,
        ArmorWeight.LIGHT, ManaTier.MEDIUM
    ),
    APOTHECARY(
        "apothecary", "Apothecary",
        "An alchemist using supplies instead of mana.",
        CyberClass.ALCHEMIST,
        false, false, false, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE
    ),
    BOMBARDIER(
        "bombardier", "Bombardier",
        "Ranged weapons and alchemical supplies; no spells.",
        CyberClass.ALCHEMIST,
        false, false, true, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE
    ),
    TRANSMUTER(
        "transmuter", "Transmuter",
        "Light armour, no mana or standard combat weapons.",
        CyberClass.ALCHEMIST,
        false, false, false, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE
    );

    public static final int REQUIRED_LEVEL = 20;

    private final String id;
    private final String displayName;
    private final String description;
    private final CyberClass baseClass;
    private final ClassRules rules;

    ClassAdvancement(
        String id,
        String displayName,
        String description,
        CyberClass baseClass,
        boolean swords,
        boolean axes,
        boolean ranged,
        boolean magic,
        boolean shields,
        ArmorWeight maxArmor,
        ManaTier manaTier
    ) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.baseClass = baseClass;
        this.rules = new ClassRules(
            swords,
            axes,
            ranged,
            magic,
            shields,
            maxArmor,
            manaTier
        );
    }

    public String id() { return id; }
    public String displayName() { return displayName; }
    public String description() { return description; }
    public CyberClass baseClass() { return baseClass; }
    public ClassRules rules() { return rules; }
    public ManaTier manaTier() { return rules.manaTier(); }

    public static Optional<ClassAdvancement> byId(String id) {
        if (id == null) {
            return Optional.empty();
        }

        String normalised = id.trim().toLowerCase(Locale.ROOT);
        return Arrays.stream(values())
            .filter(value -> value.id.equals(normalised))
            .findFirst();
    }

    public static ClassAdvancement[] choicesFor(CyberClass baseClass) {
        if (baseClass == null) {
            return new ClassAdvancement[0];
        }

        return Arrays.stream(values())
            .filter(value -> value.baseClass == baseClass)
            .toArray(ClassAdvancement[]::new);
    }

    public static Optional<ClassAdvancement> randomFor(
        CyberClass baseClass,
        RandomSource random
    ) {
        ClassAdvancement[] choices = choicesFor(baseClass);

        if (choices.length == 0 || random == null) {
            return Optional.empty();
        }

        return Optional.of(
            choices[random.nextInt(choices.length)]
        );
    }
}
