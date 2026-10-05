package com.cyberspectraa.cyberclasses.classdata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public final class CyberProgressionBridge {
    private static final String ROOT_KEY = "CyberProgression";
    private static final String LEVEL_KEY = "Level";

    private CyberProgressionBridge() {
    }

    public static int getLevel(LivingEntity entity) {
        if (entity == null) {
            return 1;
        }

        CompoundTag persistent = entity.getPersistentData();

        if (!persistent.contains(ROOT_KEY)) {
            return 1;
        }

        CompoundTag root = persistent.getCompound(ROOT_KEY);
        return Math.max(1, root.getInt(LEVEL_KEY));
    }
}
