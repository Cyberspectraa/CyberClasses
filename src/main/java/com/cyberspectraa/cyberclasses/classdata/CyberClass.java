package com.cyberspectraa.cyberclasses.classdata;

import net.minecraft.util.RandomSource;
import net.minecraftforge.fml.ModList;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum CyberClass {
    CLASSLESS(
        "classless", "Classless",
        "No formal combat class.",
        false, false, false, false, false,
        ArmorWeight.NONE,
        false, 65, false
    ),
    KNIGHT(
        "knight", "Knight",
        "Heavy frontline fighter built around melee weapons and shields.",
        true, true, false, false, true,
        ArmorWeight.HEAVY,
        true, 6, false
    ),
    ARCHER(
        "archer", "Archer",
        "Dedicated ranged fighter with swords as a backup weapon.",
        true, false, true, false, false,
        ArmorWeight.MEDIUM,
        true, 8, false
    ),
    ROGUE(
        "rogue", "Rogue",
        "Light-armoured skirmisher focused on blades and mobility.",
        true, false, false, false, false,
        ArmorWeight.LIGHT,
        true, 5, false
    ),
    MAGE(
        "mage", "Mage",
        "Full spellcaster. Gives up conventional combat gear for magic.",
        false, false, false, true, false,
        ArmorWeight.LIGHT,
        true, 3, true
    ),
    BERSERKER(
        "berserker", "Berserker",
        "Aggressive melee fighter who trades shields and magic for raw weapons.",
        true, true, false, false, false,
        ArmorWeight.HEAVY,
        true, 5, false
    ),
    CLERIC(
        "cleric", "Cleric",
        "Armoured support caster able to combine axes, shields and magic.",
        false, true, false, true, true,
        ArmorWeight.MEDIUM,
        true, 2, true
    ),
    SPELLBLADE(
        "spellblade", "Spellblade",
        "Hybrid sword-and-magic class with medium armour.",
        true, false, false, true, false,
        ArmorWeight.MEDIUM,
        true, 3, true
    ),
    RANGER(
        "ranger", "Ranger",
        "Flexible wilderness fighter using blades, axes and ranged weapons.",
        true, true, true, false, false,
        ArmorWeight.MEDIUM,
        true, 3, false
    );

    private final String id;
    private final String displayName;
    private final String description;
    private final boolean swords;
    private final boolean axes;
    private final boolean ranged;
    private final boolean magic;
    private final boolean shields;
    private final ArmorWeight maxArmor;
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
        boolean playerSelectable,
        int npcSpawnWeight,
        boolean npcRequiresIronSpells
    ) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.swords = swords;
        this.axes = axes;
        this.ranged = ranged;
        this.magic = magic;
        this.shields = shields;
        this.maxArmor = maxArmor;
        this.playerSelectable = playerSelectable;
        this.npcSpawnWeight = npcSpawnWeight;
        this.npcRequiresIronSpells = npcRequiresIronSpells;
    }

    public String id() { return id; }
    public String serializedName() { return id; }
    public String displayName() { return displayName; }
    public String description() { return description; }
    public boolean allowsSwords() { return swords; }
    public boolean allowsAxes() { return axes; }
    public boolean allowsRanged() { return ranged; }
    public boolean allowsMagic() { return magic; }
    public boolean allowsShields() { return shields; }
    public ArmorWeight maxArmor() { return maxArmor; }
    public boolean playerSelectable() { return playerSelectable; }
    public int npcSpawnWeight() { return npcSpawnWeight; }
    public boolean npcRequiresIronSpells() { return npcRequiresIronSpells; }

    public boolean usesSpellBook() {
        return this == MAGE || this == CLERIC || this == SPELLBLADE;
    }

    public boolean hasMagicSchool() {
        return usesSpellBook();
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
            if (!value.isAvailableForNpc()) {
                continue;
            }
            total += value.npcSpawnWeight;
        }

        if (total <= 0) {
            return CLASSLESS;
        }

        int roll = random.nextInt(total);

        for (CyberClass value : values()) {
            if (!value.isAvailableForNpc()) {
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
