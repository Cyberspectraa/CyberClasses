package com.cyberspectraa.cyberclasses.network.packet;

import com.cyberspectraa.cyberclasses.client.ClientPacketHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record CloseClassCreatorPacket() {
    public static void encode(
        CloseClassCreatorPacket packet,
        FriendlyByteBuf buffer
    ) {
    }

    public static CloseClassCreatorPacket decode(
        FriendlyByteBuf buffer
    ) {
        return new CloseClassCreatorPacket();
    }

    public static void handle(
        CloseClassCreatorPacket packet,
        Supplier<NetworkEvent.Context> contextSupplier
    ) {
        NetworkEvent.Context context = contextSupplier.get();

        DistExecutor.unsafeRunWhenOn(
            Dist.CLIENT,
            () -> ClientPacketHandlers::closeClassCreator
        );

        context.setPacketHandled(true);
    }
}
