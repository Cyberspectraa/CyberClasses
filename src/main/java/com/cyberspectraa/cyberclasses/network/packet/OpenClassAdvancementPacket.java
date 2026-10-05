package com.cyberspectraa.cyberclasses.network.packet;

import com.cyberspectraa.cyberclasses.client.ClientPacketHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record OpenClassAdvancementPacket(String baseClassId) {
    public static void encode(
        OpenClassAdvancementPacket packet,
        FriendlyByteBuf buffer
    ) {
        buffer.writeUtf(packet.baseClassId(), 32);
    }

    public static OpenClassAdvancementPacket decode(
        FriendlyByteBuf buffer
    ) {
        return new OpenClassAdvancementPacket(
            buffer.readUtf(32)
        );
    }

    public static void handle(
        OpenClassAdvancementPacket packet,
        Supplier<NetworkEvent.Context> contextSupplier
    ) {
        NetworkEvent.Context context = contextSupplier.get();

        DistExecutor.unsafeRunWhenOn(
            Dist.CLIENT,
            () -> () ->
                ClientPacketHandlers.openClassAdvancement(
                    packet.baseClassId()
                )
        );

        context.setPacketHandled(true);
    }
}
