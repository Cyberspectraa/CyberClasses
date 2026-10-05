package com.cyberspectraa.cyberclasses.classdata;

public enum ArmorWeight {
    NONE(0, "None"),
    LIGHT(1, "Light"),
    MEDIUM(2, "Medium"),
    HEAVY(3, "Heavy");

    private final int rank;
    private final String displayName;

    ArmorWeight(int rank, String displayName) {
        this.rank = rank;
        this.displayName = displayName;
    }

    public int rank() {
        return rank;
    }

    public String displayName() {
        return displayName;
    }

    public boolean allows(ArmorWeight other) {
        return other == null || other.rank <= this.rank;
    }
}
