package com.cyberspectraa.cyberclasses.command;

import com.cyberspectraa.cyberclasses.classdata.ClassAdvancement;
import com.cyberspectraa.cyberclasses.classdata.ClassAdvancementManager;
import com.cyberspectraa.cyberclasses.classdata.ClassCreationManager;
import com.cyberspectraa.cyberclasses.classdata.ClassManager;
import com.cyberspectraa.cyberclasses.classdata.CyberClass;
import com.cyberspectraa.cyberclasses.classdata.CyberProgressionBridge;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Arrays;

public final class CyberClassCommands {
    private CyberClassCommands() {
    }

    @SubscribeEvent
    public static void onRegisterCommands(
        RegisterCommandsEvent event
    ) {
        event.getDispatcher().register(
            Commands.literal("cyberclasses")
                .then(
                    Commands.literal("info")
                        .executes(context ->
                            info(
                                context.getSource()
                                    .getPlayerOrException()
                            )
                        )
                )
                .then(
                    Commands.literal("advance")
                        .executes(context ->
                            openAdvancement(
                                context.getSource()
                                    .getPlayerOrException()
                            )
                        )
                )
                .then(
                    Commands.literal("set")
                        .requires(source ->
                            source.hasPermission(2)
                        )
                        .then(
                            Commands.argument(
                                    "player",
                                    EntityArgument.player()
                                )
                                .then(
                                    Commands.argument(
                                            "class",
                                            StringArgumentType.word()
                                        )
                                        .suggests((context, builder) ->
                                            SharedSuggestionProvider.suggest(
                                                Arrays.stream(
                                                    CyberClass.playerChoices()
                                                ).map(CyberClass::id),
                                                builder
                                            )
                                        )
                                        .executes(context ->
                                            set(
                                                EntityArgument.getPlayer(
                                                    context,
                                                    "player"
                                                ),
                                                StringArgumentType.getString(
                                                    context,
                                                    "class"
                                                )
                                            )
                                        )
                                )
                        )
                )
                .then(
                    Commands.literal("setadvancement")
                        .requires(source ->
                            source.hasPermission(2)
                        )
                        .then(
                            Commands.argument(
                                    "player",
                                    EntityArgument.player()
                                )
                                .then(
                                    Commands.argument(
                                            "advancement",
                                            StringArgumentType.word()
                                        )
                                        .suggests((context, builder) ->
                                            SharedSuggestionProvider.suggest(
                                                Arrays.stream(
                                                    ClassAdvancement.values()
                                                ).map(ClassAdvancement::id),
                                                builder
                                            )
                                        )
                                        .executes(context ->
                                            setAdvancement(
                                                EntityArgument.getPlayer(
                                                    context,
                                                    "player"
                                                ),
                                                StringArgumentType.getString(
                                                    context,
                                                    "advancement"
                                                )
                                            )
                                        )
                                )
                        )
                )
                .then(
                    Commands.literal("clearadvancement")
                        .requires(source ->
                            source.hasPermission(2)
                        )
                        .then(
                            Commands.argument(
                                    "player",
                                    EntityArgument.player()
                                )
                                .executes(context ->
                                    clearAdvancement(
                                        EntityArgument.getPlayer(
                                            context,
                                            "player"
                                        )
                                    )
                                )
                        )
                )
                .then(
                    Commands.literal("reset")
                        .requires(source ->
                            source.hasPermission(2)
                        )
                        .then(
                            Commands.argument(
                                    "player",
                                    EntityArgument.player()
                                )
                                .executes(context ->
                                    reset(
                                        EntityArgument.getPlayer(
                                            context,
                                            "player"
                                        )
                                    )
                                )
                        )
                )
        );
    }

    private static int info(ServerPlayer player) {
        CyberClass baseClass =
            ClassManager.getClass(player).orElse(null);

        if (baseClass == null) {
            player.sendSystemMessage(
                Component.literal(
                    "CyberClass: No class selected"
                )
            );
            return 0;
        }

        var advancement =
            ClassManager.getAdvancement(player);
        var rules =
            ClassManager.getEffectiveRules(player);

        player.sendSystemMessage(
            Component.literal(
                "CyberClass: "
                    + baseClass.displayName()
                    + advancement
                        .map(value ->
                            " -> "
                                + value.displayName()
                        )
                        .orElse("")
            )
        );

        player.sendSystemMessage(
            Component.literal(
                "Cyber Level: "
                    + CyberProgressionBridge.getLevel(player)
                    + " | Mana: "
                    + rules.manaTier().displayName()
                    + " | Armour: "
                    + rules.maxArmor().displayName()
            )
        );

        if (ClassManager.isAdvancementEligible(player)) {
            player.sendSystemMessage(
                Component.literal(
                    "Class advancement available. Use /cyberclasses advance."
                )
            );
        }

        return 1;
    }

    private static int openAdvancement(
        ServerPlayer player
    ) {
        if (ClassManager.hasAdvancement(player)) {
            player.sendSystemMessage(
                Component.literal(
                    "You have already chosen "
                        + ClassManager.getAdvancement(player)
                            .map(ClassAdvancement::displayName)
                            .orElse("an advancement")
                        + "."
                )
            );
            return 0;
        }

        if (!ClassManager.isAdvancementEligible(player)) {
            player.sendSystemMessage(
                Component.literal(
                    "Class advancement unlocks at Cyber Level "
                        + ClassAdvancement.REQUIRED_LEVEL
                        + "."
                )
            );
            return 0;
        }

        ClassAdvancementManager.openSelection(player);
        return 1;
    }

    private static int set(
        ServerPlayer player,
        String id
    ) {
        CyberClass playerClass =
            CyberClass.playerById(id).orElse(null);

        if (playerClass == null) {
            player.sendSystemMessage(
                Component.literal(
                    "Unknown class: " + id
                )
            );
            return 0;
        }

        ClassCreationManager.forceSet(
            player,
            playerClass
        );

        player.sendSystemMessage(
            Component.literal(
                "Class set to "
                    + playerClass.displayName()
            )
        );

        return 1;
    }

    private static int setAdvancement(
        ServerPlayer player,
        String id
    ) {
        ClassAdvancement advancement =
            ClassAdvancement.byId(id).orElse(null);

        if (advancement == null) {
            player.sendSystemMessage(
                Component.literal(
                    "Unknown class advancement: " + id
                )
            );
            return 0;
        }

        CyberClass baseClass =
            ClassManager.getClass(player).orElse(null);

        if (baseClass != advancement.baseClass()) {
            player.sendSystemMessage(
                Component.literal(
                    advancement.displayName()
                        + " requires base class "
                        + advancement.baseClass()
                            .displayName()
                        + "."
                )
            );
            return 0;
        }

        ClassAdvancementManager.forceSet(
            player,
            advancement
        );

        player.sendSystemMessage(
            Component.literal(
                "Class advancement set to "
                    + advancement.displayName()
            )
        );

        return 1;
    }

    private static int clearAdvancement(
        ServerPlayer player
    ) {
        ClassManager.clearAdvancement(player);

        player.sendSystemMessage(
            Component.literal(
                "Class advancement cleared."
            )
        );

        return 1;
    }

    private static int reset(ServerPlayer player) {
        ClassCreationManager.reset(player);

        player.sendSystemMessage(
            Component.literal(
                "CyberClass reset. Choose a class again."
            )
        );

        return 1;
    }
}
