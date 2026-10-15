package com.cyberspectraa.cyberclasses.client;

import com.cyberspectraa.cyberclasses.classdata.ClassRules;
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
        int top = Math.max(18, height / 2 - 118);

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
                .bounds(centerX - 70, top + 194, 140, 22)
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
        int top = Math.max(18, height / 2 - 118);
        CyberClass playerClass = currentClass();
        ClassRules rules = playerClass.rules();

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
                "Your class sets which weapons, armour and spells you can use."
            ),
            centerX,
            top + 18,
            0xBDBDBD
        );

        List<FormattedCharSequence> description =
            font.split(
                Component.literal(playerClass.description()),
                300
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

        graphics.drawCenteredString(
            font,
            Component.literal(
                "Weapons: " + allowedWeapons(rules)
            ),
            centerX,
            top + 118,
            0xA7E3A1
        );

        graphics.drawCenteredString(
            font,
            Component.literal(
                "Armour: up to "
                    + rules.maxArmor().displayName()
            ),
            centerX,
            top + 133,
            0xA7C8E3
        );

        graphics.drawCenteredString(
            font,
            Component.literal(
                "Mana: "
                    + rules.manaTier().displayName()
            ),
            centerX,
            top + 148,
            rules.hasMana()
                ? 0xC6A7E3
                : 0xD18B8B
        );

        graphics.drawCenteredString(
            font,
            Component.literal(
                rules.allowsMagic()
                    ? "Magic: allowed"
                    : "Magic: restricted"
            ),
            centerX,
            top + 163,
            rules.allowsMagic()
                ? 0xC6A7E3
                : 0xD18B8B
        );

        graphics.drawCenteredString(
            font,
            Component.literal(
                "Advanced books: Rare Lv 30, Legendary Lv 50"
            ),
            centerX,
            top + 178,
            0xE6C66B
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

    public static String allowedWeapons(
        ClassRules rules
    ) {
        StringBuilder builder = new StringBuilder();

        append(builder, rules.allowsSwords(), "Swords");
        append(builder, rules.allowsAxes(), "Combat Axes");
        append(builder, rules.allowsRanged(), "Ranged");
        append(builder, rules.allowsShields(), "Shields");

        if (builder.isEmpty()) {
            return rules.allowsMagic()
                ? "Magic focus"
                : "Class-specific / unarmed";
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
