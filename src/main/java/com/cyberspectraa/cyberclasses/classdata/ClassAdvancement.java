package com.cyberspectraa.cyberclasses.classdata;

import net.minecraft.util.RandomSource;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum ClassAdvancement {
    VANGUARD(
        "vanguard", "Vanguard",
        "Offensive battlefield knight focused on pressure and formation fighting.",
        CyberClass.KNIGHT,
        true, true, false, false, true,
        ArmorWeight.HEAVY, ManaTier.NONE
    ),
    TEMPLAR(
        "templar", "Templar",
        "Anti-magic knight trained to endure and disrupt hostile spellcasters.",
        CyberClass.KNIGHT,
        true, true, false, false, true,
        ArmorWeight.HEAVY, ManaTier.NONE
    ),
    GUARDIAN(
        "guardian", "Guardian",
        "Protector who specialises in shields, ally defence and battlefield control.",
        CyberClass.KNIGHT,
        true, false, false, false, true,
        ArmorWeight.HEAVY, ManaTier.NONE
    ),
    REAVER(
        "reaver", "Reaver",
        "Risk-reward berserker who converts aggression and lost health into damage.",
        CyberClass.BERSERKER,
        true, true, false, false, false,
        ArmorWeight.HEAVY, ManaTier.NONE
    ),
    JUGGERNAUT(
        "juggernaut", "Juggernaut",
        "Relentless heavy fighter built around toughness and unstoppable momentum.",
        CyberClass.BERSERKER,
        true, true, false, false, false,
        ArmorWeight.HEAVY, ManaTier.NONE
    ),
    BLOODRAGER(
        "bloodrager", "Bloodrager",
        "Berserker path that becomes increasingly dangerous while wounded.",
        CyberClass.BERSERKER,
        true, true, false, false, false,
        ArmorWeight.HEAVY, ManaTier.NONE
    ),
    MARKSMAN(
        "marksman", "Marksman",
        "Precision archer specialising in controlled long-range attacks.",
        CyberClass.ARCHER,
        true, false, true, false, false,
        ArmorWeight.MEDIUM, ManaTier.NONE
    ),
    SHARPSHOOTER(
        "sharpshooter", "Sharpshooter",
        "High-damage archer built around critical projectile hits.",
        CyberClass.ARCHER,
        true, false, true, false, false,
        ArmorWeight.MEDIUM, ManaTier.NONE
    ),
    ARCANE_ARCHER(
        "arcane_archer", "Arcane Archer",
        "Archer who unlocks a small mana pool to empower ranged attacks.",
        CyberClass.ARCHER,
        true, false, true, true, false,
        ArmorWeight.MEDIUM, ManaTier.LOW_MEDIUM
    ),
    HUNTER(
        "hunter", "Hunter",
        "Ranger specialising in tracked targets, creatures and monster hunting.",
        CyberClass.RANGER,
        true, true, true, false, false,
        ArmorWeight.MEDIUM, ManaTier.NONE
    ),
    PATHFINDER(
        "pathfinder", "Pathfinder",
        "Exploration-focused ranger with superior terrain and travel utility.",
        CyberClass.RANGER,
        true, true, true, false, false,
        ArmorWeight.MEDIUM, ManaTier.NONE
    ),
    WARDEN(
        "warden", "Warden",
        "Protective wilderness ranger who excels at defending allies and territory.",
        CyberClass.RANGER,
        true, true, true, false, false,
        ArmorWeight.MEDIUM, ManaTier.NONE
    ),
    ASSASSIN(
        "assassin", "Assassin",
        "Rogue path focused on stealth, opening attacks and burst damage.",
        CyberClass.ROGUE,
        true, false, false, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE
    ),
    DUELIST(
        "duelist", "Duelist",
        "Technical one-on-one fighter built around timing and precise blade work.",
        CyberClass.ROGUE,
        true, false, false, false, false,
        ArmorWeight.MEDIUM, ManaTier.NONE
    ),
    SHADOW(
        "shadow", "Shadow",
        "Evasive rogue specialising in repositioning, concealment and misdirection.",
        CyberClass.ROGUE,
        true, false, false, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE
    ),
    IRON_FIST(
        "iron_fist", "Iron Fist",
        "Monk path focused on raw unarmed power and close-range pressure.",
        CyberClass.MONK,
        false, false, false, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE
    ),
    WINDWALKER(
        "windwalker", "Windwalker",
        "Highly mobile monk built around movement, dodging and rapid combinations.",
        CyberClass.MONK,
        false, false, false, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE
    ),
    ASCETIC(
        "ascetic", "Ascetic",
        "Disciplined monk focused on defence, resistance and controlled combat.",
        CyberClass.MONK,
        false, false, false, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE
    ),
    SORCERER(
        "sorcerer", "Sorcerer",
        "Mage path that sacrifices some flexibility for overwhelming spell power.",
        CyberClass.MAGE,
        false, false, false, true, false,
        ArmorWeight.LIGHT, ManaTier.VERY_HIGH
    ),
    ELEMENTALIST(
        "elementalist", "Elementalist",
        "Mage who specialises in elemental schools and elemental damage.",
        CyberClass.MAGE,
        false, false, false, true, false,
        ArmorWeight.LIGHT, ManaTier.HIGH
    ),
    NECROMANCER(
        "necromancer", "Necromancer",
        "Death-focused mage specialising in curses, summons and attrition.",
        CyberClass.MAGE,
        false, false, false, true, false,
        ArmorWeight.LIGHT, ManaTier.HIGH
    ),
    PALADIN(
        "paladin", "Paladin",
        "Martial holy caster combining heavy armour, weapons, shields and limited mana.",
        CyberClass.CLERIC,
        true, true, false, true, true,
        ArmorWeight.HEAVY, ManaTier.LOW_MEDIUM
    ),
    ORACLE(
        "oracle", "Oracle",
        "Support cleric specialising in healing, protection and foresight.",
        CyberClass.CLERIC,
        false, false, false, true, true,
        ArmorWeight.MEDIUM, ManaTier.MEDIUM
    ),
    INQUISITOR(
        "inquisitor", "Inquisitor",
        "Combat cleric specialising in hostile magic, undead and supernatural threats.",
        CyberClass.CLERIC,
        false, true, false, true, true,
        ArmorWeight.MEDIUM, ManaTier.MEDIUM
    ),
    BATTLEMAGE(
        "battlemage", "Battlemage",
        "Armoured hybrid who leans further into casting while remaining melee-capable.",
        CyberClass.SPELLBLADE,
        true, false, false, true, false,
        ArmorWeight.HEAVY, ManaTier.MEDIUM
    ),
    RUNE_KNIGHT(
        "rune_knight", "Rune Knight",
        "Hybrid warrior focused on enchanted weapons, runes and durable equipment.",
        CyberClass.SPELLBLADE,
        true, true, false, true, true,
        ArmorWeight.HEAVY, ManaTier.LOW_MEDIUM
    ),
    ARCANE_DUELIST(
        "arcane_duelist", "Arcane Duelist",
        "Fast spellblade specialising in fluid sword-and-spell combinations.",
        CyberClass.SPELLBLADE,
        true, false, false, true, false,
        ArmorWeight.MEDIUM, ManaTier.LOW_MEDIUM
    ),
    SHAMAN(
        "shaman", "Shaman",
        "Druid path centred on spirits, support magic and supernatural utility.",
        CyberClass.DRUID,
        false, false, false, true, false,
        ArmorWeight.MEDIUM, ManaTier.HIGH
    ),
    WILDSPEAKER(
        "wildspeaker", "Wildspeaker",
        "Nature specialist focused on animals, plants and wilderness magic.",
        CyberClass.DRUID,
        false, false, false, true, false,
        ArmorWeight.MEDIUM, ManaTier.HIGH
    ),
    STORMCALLER(
        "stormcaller", "Stormcaller",
        "Druid who channels weather, lightning and environmental magic.",
        CyberClass.DRUID,
        false, false, false, true, false,
        ArmorWeight.MEDIUM, ManaTier.HIGH
    ),
    SKALD(
        "skald", "Skald",
        "Combat bard combining blades with aggressive group buffs and battle songs.",
        CyberClass.BARD,
        true, false, false, true, false,
        ArmorWeight.MEDIUM, ManaTier.MEDIUM
    ),
    MINSTREL(
        "minstrel", "Minstrel",
        "Dedicated support bard focused on healing and broad team utility.",
        CyberClass.BARD,
        false, false, false, true, false,
        ArmorWeight.LIGHT, ManaTier.MEDIUM
    ),
    MESMER(
        "mesmer", "Mesmer",
        "Control-oriented bard specialising in debuffs and disruption.",
        CyberClass.BARD,
        false, false, false, true, false,
        ArmorWeight.LIGHT, ManaTier.MEDIUM
    ),
    APOTHECARY(
        "apothecary", "Apothecary",
        "Alchemist focused on healing mixtures, brewing and consumable efficiency.",
        CyberClass.ALCHEMIST,
        false, false, false, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE
    ),
    BOMBARDIER(
        "bombardier", "Bombardier",
        "Alchemist focused on thrown mixtures, explosives and area control.",
        CyberClass.ALCHEMIST,
        false, false, true, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE
    ),
    TRANSMUTER(
        "transmuter", "Transmuter",
        "Utility alchemist specialising in materials, conversion and unusual crafting.",
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
