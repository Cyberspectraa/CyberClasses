package com.cyberspectraa.cyberclasses.classdata;

public enum ManaTier {
    NONE("None", 0.0D, 0.0D),
    LOW("Low", 0.85D, 0.90D),
    LOW_MEDIUM("Low-Medium", 0.95D, 0.95D),
    MEDIUM("Medium", 1.10D, 1.05D),
    HIGH("High", 1.20D, 1.15D),
    VERY_HIGH("Very High", 1.35D, 1.25D);

    private final String displayName;
    private final double maxManaMultiplier;
    private final double manaRegenMultiplier;

    ManaTier(
        String displayName,
        double maxManaMultiplier,
        double manaRegenMultiplier
    ) {
        this.displayName = displayName;
        this.maxManaMultiplier = maxManaMultiplier;
        this.manaRegenMultiplier = manaRegenMultiplier;
    }

    public String displayName() {
        return displayName;
    }

    public double maxManaMultiplier() {
        return maxManaMultiplier;
    }

    public double manaRegenMultiplier() {
        return manaRegenMultiplier;
    }

    public boolean hasMana() {
        return this != NONE;
    }
}
