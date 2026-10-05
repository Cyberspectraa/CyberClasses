package com.cyberspectraa.cyberclasses.command;

import com.cyberspectraa.cyberclasses.classdata.ClassCreationManager;
import com.cyberspectraa.cyberclasses.classdata.ClassManager;
import com.cyberspectraa.cyberclasses.classdata.PlayerClass;
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
                            info(context.getSource().getPlayerOrException())
                        )
                )
                .then(
                    Commands.literal("set")
                        .requires(source -> source.hasPermission(2))
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
                                                    PlayerClass.values()
                                                ).map(PlayerClass::id),
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
                    Commands.literal("reset")
                        .requires(source -> source.hasPermission(2))
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
        String value = ClassManager.getClass(player)
            .map(playerClass ->
                playerClass.displayName()
                    + " — "
                    + playerClass.description()
            )
            .orElse("No class selected");

        player.sendSystemMessage(
            Component.literal(
                "CyberClass: " + value
            )
        );

        return 1;
    }

    private static int set(
        ServerPlayer player,
        String id
    ) {
        PlayerClass playerClass =
            PlayerClass.byId(id).orElse(null);

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
