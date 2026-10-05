package com.cyberspectraa.cyberclasses.client;

import com.cyberspectraa.cyberclasses.classdata.CyberClass;
import com.cyberspectraa.cyberclasses.network.CyberClassesNetwork;
import com.cyberspectraa.cyberclasses.network.packet.SubmitClassPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public final class ClassCreatorScreen extends Screen {
    private static final CyberClass[] PLAYER_CLASSES =
        CyberClass.playerChoices();

    private int classIndex;
    private Button classButton;

    public ClassCreatorScreen() {
        super(Component.literal("Choose Your Class"));
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        int top = Math.max(24, height / 2 - 105);

        addRenderableWidget(
            Button.builder(
                    Component.literal("<"),
                    button -> changeClass(-1)
                )
                .bounds(centerX - 150, top + 42, 28, 20)
                .build()
        );

        classButton = addRenderableWidget(
            Button.builder(
                    Component.literal(currentClass().displayName()),
                    button -> changeClass(1)
                )
                .bounds(centerX - 116, top + 42, 232, 20)
                .build()
        );

        addRenderableWidget(
            Button.builder(
                    Component.literal(">"),
                    button -> changeClass(1)
                )
                .bounds(centerX + 122, top + 42, 28, 20)
                .build()
        );

        addRenderableWidget(
            Button.builder(
                    Component.literal("Choose Class"),
                    button -> submit()
                )
                .bounds(centerX - 70, top + 170, 140, 22)
                .build()
        );
    }

    @Override
    public void render(
        GuiGraphics graphics,
        int mouseX,
        int mouseY,
        float partialTick
    ) {
        renderBackground(graphics);

        int centerX = width / 2;
        int top = Math.max(24, height / 2 - 105);
        CyberClass playerClass = currentClass();

        graphics.drawCenteredString(
            font,
            title,
            centerX,
            top,
            0xFFFFFF
        );

        graphics.drawCenteredString(
            font,
            Component.literal(
                "Race chosen. Now choose how your character fights and what gear they can use."
            ),
            centerX,
            top + 18,
            0xBDBDBD
        );

        List<FormattedCharSequence> description =
            font.split(
                Component.literal(playerClass.description()),
                286
            );

        int textY = top + 76;
        for (FormattedCharSequence line : description) {
            graphics.drawCenteredString(
                font,
                line,
                centerX,
                textY,
                0xE5E5E5
            );
            textY += 11;
        }

        String weapons = allowedWeapons(playerClass);

        graphics.drawCenteredString(
            font,
            Component.literal("Weapons: " + weapons),
            centerX,
            top + 116,
            0xA7E3A1
        );

        graphics.drawCenteredString(
            font,
            Component.literal(
                "Armour: up to "
                    + playerClass.maxArmor().displayName()
            ),
            centerX,
            top + 130,
            0xA7C8E3
        );

        graphics.drawCenteredString(
            font,
            Component.literal(
                playerClass.allowsMagic()
                    ? "Magic: allowed"
                    : "Magic: restricted"
            ),
            centerX,
            top + 144,
            playerClass.allowsMagic()
                ? 0xC6A7E3
                : 0xD18B8B
        );

        super.render(
            graphics,
            mouseX,
            mouseY,
            partialTick
        );
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    private void changeClass(int direction) {
        classIndex = Math.floorMod(
            classIndex + direction,
            PLAYER_CLASSES.length
        );

        if (classButton != null) {
            classButton.setMessage(
                Component.literal(
                    currentClass().displayName()
                )
            );
        }
    }

    private CyberClass currentClass() {
        return PLAYER_CLASSES[classIndex];
    }

    private void submit() {
        CyberClassesNetwork.sendToServer(
            new SubmitClassPacket(
                currentClass().id()
            )
        );
    }

    private static String allowedWeapons(
        CyberClass playerClass
    ) {
        StringBuilder builder = new StringBuilder();

        append(
            builder,
            playerClass.allowsSwords(),
            "Swords"
        );
        append(
            builder,
            playerClass.allowsAxes(),
            "Axes"
        );
        append(
            builder,
            playerClass.allowsRanged(),
            "Bows/Crossbows"
        );
        append(
            builder,
            playerClass.allowsShields(),
            "Shields"
        );

        if (builder.isEmpty()) {
            return playerClass.allowsMagic()
                ? "Magic only"
                : "None";
        }

        return builder.toString();
    }

    private static void append(
        StringBuilder builder,
        boolean allowed,
        String label
    ) {
        if (!allowed) {
            return;
        }

        if (!builder.isEmpty()) {
            builder.append(", ");
        }

        builder.append(label);
    }
}
