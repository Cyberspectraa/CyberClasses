package com.cyberspectraa.cyberclasses.classdata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public final class ClassManager {
    public static final String ROOT_KEY = "CyberClasses";
    public static final String CLASS_KEY = "Class";
    public static final String CHOSEN_KEY = "ClassChosen";
    public static final String ADVANCEMENT_KEY = "Advancement";
    public static final String ADVANCEMENT_CHOSEN_KEY = "AdvancementChosen";

    private ClassManager() {
    }

    public static Optional<CyberClass> getClass(ServerPlayer player) {
        if (player == null) {
            return Optional.empty();
        }

        CompoundTag root =
            player.getPersistentData().getCompound(ROOT_KEY);

        if (!root.getBoolean(CHOSEN_KEY)
                || !root.contains(CLASS_KEY)) {
            return Optional.empty();
        }

        return CyberClass.byId(root.getString(CLASS_KEY));
    }

    public static Optional<ClassAdvancement> getAdvancement(
        ServerPlayer player
    ) {
        if (player == null) {
            return Optional.empty();
        }

        CompoundTag root =
            player.getPersistentData().getCompound(ROOT_KEY);

        if (!root.getBoolean(ADVANCEMENT_CHOSEN_KEY)
                || !root.contains(ADVANCEMENT_KEY)) {
            return Optional.empty();
        }

        ClassAdvancement advancement =
            ClassAdvancement.byId(
                root.getString(ADVANCEMENT_KEY)
            ).orElse(null);

        CyberClass baseClass = getClass(player).orElse(null);

        if (advancement == null
                || baseClass == null
                || advancement.baseClass() != baseClass) {
            return Optional.empty();
        }

        return Optional.of(advancement);
    }

    public static boolean hasClass(ServerPlayer player) {
        return getClass(player).isPresent();
    }

    public static boolean hasAdvancement(ServerPlayer player) {
        return getAdvancement(player).isPresent();
    }

    public static boolean isAdvancementEligible(ServerPlayer player) {
        return hasClass(player)
            && !hasAdvancement(player)
            && CyberProgressionBridge.getLevel(player)
                >= ClassAdvancement.REQUIRED_LEVEL;
    }

    public static ClassRules getEffectiveRules(
        ServerPlayer player
    ) {
        return getAdvancement(player)
            .map(ClassAdvancement::rules)
            .orElseGet(() ->
                getClass(player)
                    .map(CyberClass::rules)
                    .orElse(
                        CyberClass.CLASSLESS.rules()
                    )
            );
    }

    public static String getEffectiveDisplayName(
        ServerPlayer player
    ) {
        return getAdvancement(player)
            .map(ClassAdvancement::displayName)
            .orElseGet(() ->
                getClass(player)
                    .map(CyberClass::displayName)
                    .orElse("No Class")
            );
    }

    public static void setClass(
        ServerPlayer player,
        CyberClass playerClass
    ) {
        if (player == null
                || playerClass == null
                || !playerClass.playerSelectable()) {
            return;
        }

        CompoundTag persistent = player.getPersistentData();
        CompoundTag root = persistent.contains(ROOT_KEY)
            ? persistent.getCompound(ROOT_KEY)
            : new CompoundTag();

        root.putString(CLASS_KEY, playerClass.id());
        root.putBoolean(CHOSEN_KEY, true);

        // A base-class change invalidates every advancement built on the
        // previous class. This also keeps admin class changes save-safe.
        root.remove(ADVANCEMENT_KEY);
        root.putBoolean(ADVANCEMENT_CHOSEN_KEY, false);

        persistent.put(ROOT_KEY, root);
    }

    public static boolean setAdvancement(
        ServerPlayer player,
        ClassAdvancement advancement
    ) {
        if (player == null || advancement == null) {
            return false;
        }

        CyberClass baseClass = getClass(player).orElse(null);

        if (baseClass == null
                || advancement.baseClass() != baseClass
                || CyberProgressionBridge.getLevel(player)
                    < ClassAdvancement.REQUIRED_LEVEL) {
            return false;
        }

        CompoundTag persistent = player.getPersistentData();
        CompoundTag root = persistent.contains(ROOT_KEY)
            ? persistent.getCompound(ROOT_KEY)
            : new CompoundTag();

        root.putString(
            ADVANCEMENT_KEY,
            advancement.id()
        );
        root.putBoolean(
            ADVANCEMENT_CHOSEN_KEY,
            true
        );
        persistent.put(ROOT_KEY, root);
        return true;
    }

    public static void forceSetAdvancement(
        ServerPlayer player,
        ClassAdvancement advancement
    ) {
        if (player == null || advancement == null) {
            return;
        }

        CyberClass baseClass = getClass(player).orElse(null);

        if (baseClass == null
                || advancement.baseClass() != baseClass) {
            return;
        }

        CompoundTag persistent = player.getPersistentData();
        CompoundTag root = persistent.contains(ROOT_KEY)
            ? persistent.getCompound(ROOT_KEY)
            : new CompoundTag();

        root.putString(
            ADVANCEMENT_KEY,
            advancement.id()
        );
        root.putBoolean(
            ADVANCEMENT_CHOSEN_KEY,
            true
        );
        persistent.put(ROOT_KEY, root);
    }

    public static void clearAdvancement(ServerPlayer player) {
        if (player == null) {
            return;
        }

        CompoundTag persistent = player.getPersistentData();
        CompoundTag root = persistent.contains(ROOT_KEY)
            ? persistent.getCompound(ROOT_KEY)
            : new CompoundTag();

        root.remove(ADVANCEMENT_KEY);
        root.putBoolean(ADVANCEMENT_CHOSEN_KEY, false);
        persistent.put(ROOT_KEY, root);
    }

    public static void clearClass(ServerPlayer player) {
        CompoundTag persistent = player.getPersistentData();
        CompoundTag root = persistent.contains(ROOT_KEY)
            ? persistent.getCompound(ROOT_KEY)
            : new CompoundTag();

        root.remove(CLASS_KEY);
        root.putBoolean(CHOSEN_KEY, false);
        root.remove(ADVANCEMENT_KEY);
        root.putBoolean(ADVANCEMENT_CHOSEN_KEY, false);
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
