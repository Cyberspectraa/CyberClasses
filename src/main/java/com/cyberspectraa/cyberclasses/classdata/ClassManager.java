package com.cyberspectraa.cyberclasses.classdata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public final class ClassManager {
    public static final String ROOT_KEY = "CyberClasses";
    public static final String CLASS_KEY = "Class";
    public static final String CHOSEN_KEY = "ClassChosen";

    private ClassManager() {
    }

    public static Optional<PlayerClass> getClass(ServerPlayer player) {
        if (player == null) {
            return Optional.empty();
        }

        CompoundTag root =
            player.getPersistentData().getCompound(ROOT_KEY);

        if (!root.getBoolean(CHOSEN_KEY)
                || !root.contains(CLASS_KEY)) {
            return Optional.empty();
        }

        return PlayerClass.byId(root.getString(CLASS_KEY));
    }

    public static boolean hasClass(ServerPlayer player) {
        return getClass(player).isPresent();
    }

    public static void setClass(
        ServerPlayer player,
        PlayerClass playerClass
    ) {
        CompoundTag persistent = player.getPersistentData();
        CompoundTag root = persistent.contains(ROOT_KEY)
            ? persistent.getCompound(ROOT_KEY)
            : new CompoundTag();

        root.putString(CLASS_KEY, playerClass.id());
        root.putBoolean(CHOSEN_KEY, true);
        persistent.put(ROOT_KEY, root);
    }

    public static void clearClass(ServerPlayer player) {
        CompoundTag persistent = player.getPersistentData();
        CompoundTag root = persistent.contains(ROOT_KEY)
            ? persistent.getCompound(ROOT_KEY)
            : new CompoundTag();

        root.remove(CLASS_KEY);
        root.putBoolean(CHOSEN_KEY, false);
        persistent.put(ROOT_KEY, root);
    }

    public static void copyData(
        ServerPlayer oldPlayer,
        ServerPlayer newPlayer
    ) {
        CompoundTag oldPersistent = oldPlayer.getPersistentData();

        if (oldPersistent.contains(ROOT_KEY)) {
            newPlayer.getPersistentData().put(
                ROOT_KEY,
                oldPersistent.getCompound(ROOT_KEY).copy()
            );
        }
    }
}
