package com.cyberspectraa.cyberclasses.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ClientPacketHandlers {
    private ClientPacketHandlers() {
    }

    public static void openClassCreator() {
        Minecraft minecraft = Minecraft.getInstance();

        if (!(minecraft.screen instanceof ClassCreatorScreen)) {
            minecraft.setScreen(new ClassCreatorScreen());
        }
    }

    public static void closeClassCreator() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.screen instanceof ClassCreatorScreen) {
            minecraft.setScreen(null);
        }
    }

    public static void openClassAdvancement(
        String baseClassId
    ) {
        Minecraft minecraft = Minecraft.getInstance();

        if (!(minecraft.screen
                instanceof ClassAdvancementScreen)) {
            minecraft.setScreen(
                new ClassAdvancementScreen(baseClassId)
            );
        }
    }

    public static void closeClassAdvancement() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.screen
                instanceof ClassAdvancementScreen) {
            minecraft.setScreen(null);
        }
    }
}
