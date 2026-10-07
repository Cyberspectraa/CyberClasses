package com.cyberspectraa.cyberclasses.classdata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Persistent class-scoped unlock flags granted by class quests.
 *
 * CyberQuest grants symbolic ids instead of knowing about concrete class
 * abilities. CyberClasses features can check these flags later without
 * tightly coupling the two mods.
 */
public final class ClassQuestUnlockManager {
    public static final String QUEST_UNLOCKS_KEY = "QuestUnlocks";

    private ClassQuestUnlockManager() {
    }

    public static boolean unlock(
        ServerPlayer player,
        String classId,
        String unlockId
    ) {
        String normalizedClass = normalize(classId);
        String normalizedUnlock = normalize(unlockId);

        if (player == null
                || normalizedClass.isBlank()
                || normalizedUnlock.isBlank()) {
            return false;
        }

        CyberClass current =
            ClassManager.getClass(player).orElse(null);

        if (current == null
                || !current.id().equals(normalizedClass)) {
            return false;
        }

        CompoundTag root = classRoot(player);
        CompoundTag unlocks = root.contains(
                QUEST_UNLOCKS_KEY,
                Tag.TAG_COMPOUND
            )
            ? root.getCompound(QUEST_UNLOCKS_KEY)
            : new CompoundTag();

        ListTag classUnlocks = unlocks.getList(
            normalizedClass,
            Tag.TAG_STRING
        );

        for (int i = 0; i < classUnlocks.size(); i++) {
            if (normalizedUnlock.equals(
                    classUnlocks.getString(i)
            )) {
                return false;
            }
        }

        classUnlocks.add(
            StringTag.valueOf(normalizedUnlock)
        );
        unlocks.put(
            normalizedClass,
            classUnlocks
        );
        root.put(
            QUEST_UNLOCKS_KEY,
            unlocks
        );
        player.getPersistentData().put(
            ClassManager.ROOT_KEY,
            root
        );
        return true;
    }

    public static boolean hasUnlock(
        ServerPlayer player,
        String unlockId
    ) {
        CyberClass current =
            ClassManager.getClass(player).orElse(null);

        return current != null
            && hasUnlock(
                player,
                current.id(),
                unlockId
            );
    }

    public static boolean hasUnlock(
        ServerPlayer player,
        String classId,
        String unlockId
    ) {
        if (player == null) {
            return false;
        }

        String normalizedClass = normalize(classId);
        String normalizedUnlock = normalize(unlockId);

        if (normalizedClass.isBlank()
                || normalizedUnlock.isBlank()) {
            return false;
        }

        CompoundTag root =
            player.getPersistentData()
                .getCompound(ClassManager.ROOT_KEY);

        if (!root.contains(
                QUEST_UNLOCKS_KEY,
                Tag.TAG_COMPOUND
        )) {
            return false;
        }

        CompoundTag unlocks =
            root.getCompound(QUEST_UNLOCKS_KEY);

        ListTag classUnlocks =
            unlocks.getList(
                normalizedClass,
                Tag.TAG_STRING
            );

        for (int i = 0; i < classUnlocks.size(); i++) {
            if (normalizedUnlock.equals(
                    classUnlocks.getString(i)
            )) {
                return true;
            }
        }

        return false;
    }

    public static Set<String> unlocks(
        ServerPlayer player,
        String classId
    ) {
        Set<String> result =
            new LinkedHashSet<>();

        if (player == null) {
            return result;
        }

        String normalizedClass =
            normalize(classId);

        CompoundTag root =
            player.getPersistentData()
                .getCompound(ClassManager.ROOT_KEY);

        CompoundTag unlocks =
            root.getCompound(QUEST_UNLOCKS_KEY);

        ListTag classUnlocks =
            unlocks.getList(
                normalizedClass,
                Tag.TAG_STRING
            );

        for (int i = 0; i < classUnlocks.size(); i++) {
            String value =
                normalize(
                    classUnlocks.getString(i)
                );

            if (!value.isBlank()) {
                result.add(value);
            }
        }

        return result;
    }

    private static CompoundTag classRoot(
        ServerPlayer player
    ) {
        CompoundTag persistent =
            player.getPersistentData();

        return persistent.contains(
                ClassManager.ROOT_KEY,
                Tag.TAG_COMPOUND
            )
            ? persistent.getCompound(
                ClassManager.ROOT_KEY
            )
            : new CompoundTag();
    }

    private static String normalize(
        String value
    ) {
        return value == null
            ? ""
            : value.trim()
                .toLowerCase(Locale.ROOT);
    }
}
