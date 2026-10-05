package com.cyberspectraa.cyberclasses.network.packet;

import com.cyberspectraa.cyberclasses.client.ClientPacketHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record OpenClassCreatorPacket() {
    public static void encode(
        OpenClassCreatorPacket packet,
        FriendlyByteBuf buffer
    ) {
    }

    public static OpenClassCreatorPacket decode(
        FriendlyByteBuf buffer
    ) {
        return new OpenClassCreatorPacket();
    }

    public static void handle(
        OpenClassCreatorPacket packet,
        Supplier<NetworkEvent.Context> contextSupplier
    ) {
        NetworkEvent.Context context = contextSupplier.get();

        DistExecutor.unsafeRunWhenOn(
            Dist.CLIENT,
            () -> ClientPacketHandlers::openClassCreator
        );

        context.setPacketHandled(true);
    }
}
