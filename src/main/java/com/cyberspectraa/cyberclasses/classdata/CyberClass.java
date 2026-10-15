package com.cyberspectraa.cyberclasses.classdata;

import net.minecraft.util.RandomSource;
import net.minecraftforge.fml.ModList;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum CyberClass {
    CLASSLESS(
        "classless", "Classless",
        "No formal class or special combat training.",
        false, false, false, false, false,
        ArmorWeight.NONE, ManaTier.NONE,
        false, 65, false
    ),
    KNIGHT(
        "knight", "Knight",
        "Heavy armour, swords, axes and shields. No spells.",
        true, true, false, false, true,
        ArmorWeight.HEAVY, ManaTier.NONE,
        true, 6, false
    ),
    BERSERKER(
        "berserker", "Berserker",
        "Swords and axes in heavy armour, without a shield.",
        true, true, false, false, false,
        ArmorWeight.HEAVY, ManaTier.NONE,
        true, 5, false
    ),
    ARCHER(
        "archer", "Archer",
        "Ranged weapons, with a sword for close fights.",
        true, false, true, false, false,
        ArmorWeight.MEDIUM, ManaTier.NONE,
        true, 8, false
    ),
    RANGER(
        "ranger", "Ranger",
        "Swords, axes and bows, with medium armour.",
        true, true, true, false, false,
        ArmorWeight.MEDIUM, ManaTier.NONE,
        true, 3, false
    ),
    ROGUE(
        "rogue", "Rogue",
        "Light armour and a sword. No shields or spells.",
        true, false, false, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE,
        true, 5, false
    ),
    MONK(
        "monk", "Monk",
        "Light armour and unarmed fighting.",
        false, false, false, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE,
        true, 3, false
    ),
    MAGE(
        "mage", "Mage",
        "A large mana pool and spells, but only light armour.",
        false, false, false, true, false,
        ArmorWeight.LIGHT, ManaTier.VERY_HIGH,
        true, 3, true
    ),
    CLERIC(
        "cleric", "Cleric",
        "Axes, shields and magic, with medium armour.",
        false, true, false, true, true,
        ArmorWeight.MEDIUM, ManaTier.MEDIUM,
        true, 2, true
    ),
    SPELLBLADE(
        "spellblade", "Spellblade",
        "Sword and spellbook, with a smaller mana pool.",
        true, false, false, true, false,
        ArmorWeight.MEDIUM, ManaTier.LOW,
        true, 3, true
    ),
    DRUID(
        "druid", "Druid",
        "High mana for casting spells; medium armour.",
        false, false, false, true, false,
        ArmorWeight.MEDIUM, ManaTier.HIGH,
        true, 2, true
    ),
    BARD(
        "bard", "Bard",
        "Sword and spells, with light armour and medium mana.",
        true, false, false, true, false,
        ArmorWeight.LIGHT, ManaTier.MEDIUM,
        true, 2, true
    ),
    ALCHEMIST(
        "alchemist", "Alchemist",
        "Potions and supplies instead of spells or heavy weapons.",
        false, false, false, false, false,
        ArmorWeight.LIGHT, ManaTier.NONE,
        true, 2, false
    );

    private final String id;
    private final String displayName;
    private final String description;
    private final ClassRules rules;
    private final boolean playerSelectable;
    private final int npcSpawnWeight;
    private final boolean npcRequiresIronSpells;

    CyberClass(
        String id,
        String displayName,
        String description,
        boolean swords,
        boolean axes,
        boolean ranged,
        boolean magic,
        boolean shields,
        ArmorWeight maxArmor,
        ManaTier manaTier,
        boolean playerSelectable,
        int npcSpawnWeight,
        boolean npcRequiresIronSpells
    ) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.rules = new ClassRules(
            swords,
            axes,
            ranged,
            magic,
            shields,
            maxArmor,
            manaTier
        );
        this.playerSelectable = playerSelectable;
        this.npcSpawnWeight = npcSpawnWeight;
        this.npcRequiresIronSpells = npcRequiresIronSpells;
    }

    public String id() { return id; }
    public String serializedName() { return id; }
    public String displayName() { return displayName; }
    public String description() { return description; }
    public ClassRules rules() { return rules; }
    public boolean allowsSwords() { return rules.allowsSwords(); }
    public boolean allowsAxes() { return rules.allowsAxes(); }
    public boolean allowsRanged() { return rules.allowsRanged(); }
    public boolean allowsMagic() { return rules.allowsMagic(); }
    public boolean allowsShields() { return rules.allowsShields(); }
    public ArmorWeight maxArmor() { return rules.maxArmor(); }
    public ManaTier manaTier() { return rules.manaTier(); }
    public boolean playerSelectable() { return playerSelectable; }
    public int npcSpawnWeight() { return npcSpawnWeight; }
    public boolean npcRequiresIronSpells() { return npcRequiresIronSpells; }

    public boolean usesSpellBook() {
        return allowsMagic();
    }

    public boolean hasMagicSchool() {
        return allowsMagic();
    }

    public boolean isAvailableForNpc() {
        return !npcRequiresIronSpells
            || ModList.get().isLoaded("irons_spellbooks");
    }

    public static Optional<CyberClass> byId(String id) {
        if (id == null) {
            return Optional.empty();
        }

        String normalised = normalizeLegacyId(id);
        return Arrays.stream(values())
            .filter(value -> value.id.equals(normalised))
            .findFirst();
    }

    public static Optional<CyberClass> playerById(String id) {
        return byId(id).filter(CyberClass::playerSelectable);
    }

    public static CyberClass[] playerChoices() {
        return Arrays.stream(values())
            .filter(CyberClass::playerSelectable)
            .toArray(CyberClass[]::new);
    }

    public static CyberClass randomNpcClass(RandomSource random) {
        int total = 0;

        for (CyberClass value : values()) {
            if (!value.isAvailableForNpc()
                    || value.npcSpawnWeight <= 0) {
                continue;
            }
            total += value.npcSpawnWeight;
        }

        if (total <= 0) {
            return CLASSLESS;
        }

        int roll = random.nextInt(total);

        for (CyberClass value : values()) {
            if (!value.isAvailableForNpc()
                    || value.npcSpawnWeight <= 0) {
                continue;
            }

            roll -= value.npcSpawnWeight;
            if (roll < 0) {
                return value;
            }
        }

        return CLASSLESS;
    }

    public static String normalizeLegacyId(String id) {
        if (id == null || id.isBlank()) {
            return CLASSLESS.id;
        }

        String normalized = id.trim().toLowerCase(Locale.ROOT);

        if ("horse_tamer".equals(normalized)) {
            return RANGER.id;
        }

        return normalized;
    }
}
