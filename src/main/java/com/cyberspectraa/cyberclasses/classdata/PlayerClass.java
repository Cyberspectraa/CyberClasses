package com.cyberspectraa.cyberclasses.classdata;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum PlayerClass {
    KNIGHT(
        "knight", "Knight",
        "Heavy frontline fighter built around melee weapons and shields.",
        true, true, false, false, true, ArmorWeight.HEAVY
    ),
    ARCHER(
        "archer", "Archer",
        "Dedicated ranged fighter with swords as a backup weapon.",
        true, false, true, false, false, ArmorWeight.MEDIUM
    ),
    ROGUE(
        "rogue", "Rogue",
        "Light-armoured skirmisher focused on blades and mobility.",
        true, false, false, false, false, ArmorWeight.LIGHT
    ),
    MAGE(
        "mage", "Mage",
        "Full spellcaster. Gives up conventional combat gear for magic.",
        false, false, false, true, false, ArmorWeight.LIGHT
    ),
    BERSERKER(
        "berserker", "Berserker",
        "Aggressive melee fighter who trades shields and magic for raw weapons.",
        true, true, false, false, false, ArmorWeight.HEAVY
    ),
    CLERIC(
        "cleric", "Cleric",
        "Armoured support caster able to combine axes, shields and magic.",
        false, true, false, true, true, ArmorWeight.MEDIUM
    ),
    SPELLBLADE(
        "spellblade", "Spellblade",
        "Hybrid sword-and-magic class with medium armour.",
        true, false, false, true, false, ArmorWeight.MEDIUM
    ),
    RANGER(
        "ranger", "Ranger",
        "Flexible wilderness fighter using blades, axes and ranged weapons.",
        true, true, true, false, false, ArmorWeight.MEDIUM
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

    PlayerClass(
        String id,
        String displayName,
        String description,
        boolean swords,
        boolean axes,
        boolean ranged,
        boolean magic,
        boolean shields,
        ArmorWeight maxArmor
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
    }

    public String id() { return id; }
    public String displayName() { return displayName; }
    public String description() { return description; }
    public boolean allowsSwords() { return swords; }
    public boolean allowsAxes() { return axes; }
    public boolean allowsRanged() { return ranged; }
    public boolean allowsMagic() { return magic; }
    public boolean allowsShields() { return shields; }
    public ArmorWeight maxArmor() { return maxArmor; }

    public static Optional<PlayerClass> byId(String id) {
        if (id == null) {
            return Optional.empty();
        }

        String normalised = id.trim().toLowerCase(Locale.ROOT);
        return Arrays.stream(values())
            .filter(value -> value.id.equals(normalised))
            .findFirst();
    }
}
