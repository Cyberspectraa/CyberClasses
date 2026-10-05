package com.cyberspectraa.cyberclasses.client;

import com.cyberspectraa.cyberclasses.classdata.ClassAdvancement;
import com.cyberspectraa.cyberclasses.classdata.CyberClass;
import com.cyberspectraa.cyberclasses.network.CyberClassesNetwork;
import com.cyberspectraa.cyberclasses.network.packet.SubmitClassAdvancementPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public final class ClassAdvancementScreen extends Screen {
    private final CyberClass baseClass;
    private final ClassAdvancement[] choices;

    private int advancementIndex;
    private Button advancementButton;

    public ClassAdvancementScreen(String baseClassId) {
        super(Component.literal("Choose Class Advancement"));

        this.baseClass = CyberClass.byId(baseClassId)
            .orElse(CyberClass.CLASSLESS);
        this.choices =
            ClassAdvancement.choicesFor(this.baseClass);
    }

    @Override
    protected void init() {
        if (choices.length == 0) {
            onClose();
            return;
        }

        int centerX = width / 2;
        int top = Math.max(18, height / 2 - 118);

        addRenderableWidget(
            Button.builder(
                    Component.literal("<"),
                    button -> changeAdvancement(-1)
                )
                .bounds(centerX - 150, top + 45, 28, 20)
                .build()
        );

        advancementButton = addRenderableWidget(
            Button.builder(
                    Component.literal(
                        currentAdvancement().displayName()
                    ),
                    button -> changeAdvancement(1)
                )
                .bounds(centerX - 116, top + 45, 232, 20)
                .build()
        );

        addRenderableWidget(
            Button.builder(
                    Component.literal(">"),
                    button -> changeAdvancement(1)
                )
                .bounds(centerX + 122, top + 45, 28, 20)
                .build()
        );

        addRenderableWidget(
            Button.builder(
                    Component.literal("Choose Advancement"),
                    button -> submit()
                )
                .bounds(centerX - 85, top + 198, 170, 22)
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

        if (choices.length == 0) {
            super.render(graphics, mouseX, mouseY, partialTick);
            return;
        }

        int centerX = width / 2;
        int top = Math.max(18, height / 2 - 118);
        ClassAdvancement advancement =
            currentAdvancement();

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
                baseClass.displayName()
                    + " — Level 20 Advancement"
            ),
            centerX,
            top + 18,
            0xD6B8FF
        );

        List<FormattedCharSequence> description =
            font.split(
                Component.literal(
                    advancement.description()
                ),
                300
            );

        int textY = top + 80;
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

        var rules = advancement.rules();

        graphics.drawCenteredString(
            font,
            Component.literal(
                "Weapons: "
                    + ClassCreatorScreen.allowedWeapons(rules)
            ),
            centerX,
            top + 125,
            0xA7E3A1
        );

        graphics.drawCenteredString(
            font,
            Component.literal(
                "Armour: up to "
                    + rules.maxArmor().displayName()
            ),
            centerX,
            top + 140,
            0xA7C8E3
        );

        graphics.drawCenteredString(
            font,
            Component.literal(
                "Mana: "
                    + rules.manaTier().displayName()
            ),
            centerX,
            top + 155,
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
            top + 170,
            rules.allowsMagic()
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
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void changeAdvancement(int direction) {
        advancementIndex = Math.floorMod(
            advancementIndex + direction,
            choices.length
        );

        if (advancementButton != null) {
            advancementButton.setMessage(
                Component.literal(
                    currentAdvancement().displayName()
                )
            );
        }
    }

    private ClassAdvancement currentAdvancement() {
        return choices[advancementIndex];
    }

    private void submit() {
        CyberClassesNetwork.sendToServer(
            new SubmitClassAdvancementPacket(
                currentAdvancement().id()
            )
        );
    }
}
