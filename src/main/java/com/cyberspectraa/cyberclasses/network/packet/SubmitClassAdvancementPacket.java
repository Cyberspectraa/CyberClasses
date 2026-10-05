package com.cyberspectraa.cyberclasses.network.packet;

import com.cyberspectraa.cyberclasses.classdata.ClassAdvancement;
import com.cyberspectraa.cyberclasses.classdata.ClassAdvancementManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SubmitClassAdvancementPacket(String advancementId) {
    public static void encode(
        SubmitClassAdvancementPacket packet,
        FriendlyByteBuf buffer
    ) {
        buffer.writeUtf(packet.advancementId(), 48);
    }

    public static SubmitClassAdvancementPacket decode(
        FriendlyByteBuf buffer
    ) {
        return new SubmitClassAdvancementPacket(
            buffer.readUtf(48)
        );
    }

    public static void handle(
        SubmitClassAdvancementPacket packet,
        Supplier<NetworkEvent.Context> contextSupplier
    ) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer player = context.getSender();

        if (player != null) {
            ClassAdvancement.byId(
                packet.advancementId()
            ).ifPresent(advancement ->
                ClassAdvancementManager.complete(
                    player,
                    advancement
                )
            );
        }

        context.setPacketHandled(true);
    }
}
