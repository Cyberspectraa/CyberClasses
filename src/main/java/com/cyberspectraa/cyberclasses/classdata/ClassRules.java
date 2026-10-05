package com.cyberspectraa.cyberclasses.classdata;

public record ClassRules(
    boolean swords,
    boolean axes,
    boolean ranged,
    boolean magic,
    boolean shields,
    ArmorWeight maxArmor,
    ManaTier manaTier
) {
    public boolean allowsSwords() { return swords; }
    public boolean allowsAxes() { return axes; }
    public boolean allowsRanged() { return ranged; }
    public boolean allowsMagic() { return magic; }
    public boolean allowsShields() { return shields; }

    public boolean hasMana() {
        return magic && manaTier != null && manaTier.hasMana();
    }
}
