package com.cyberspectraa.cyberclasses.network.packet;

import com.cyberspectraa.cyberclasses.client.ClientPacketHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record CloseClassAdvancementPacket() {
    public static void encode(
        CloseClassAdvancementPacket packet,
        FriendlyByteBuf buffer
    ) {
    }

    public static CloseClassAdvancementPacket decode(
        FriendlyByteBuf buffer
    ) {
        return new CloseClassAdvancementPacket();
    }

    public static void handle(
        CloseClassAdvancementPacket packet,
        Supplier<NetworkEvent.Context> contextSupplier
    ) {
        NetworkEvent.Context context = contextSupplier.get();

        DistExecutor.unsafeRunWhenOn(
            Dist.CLIENT,
            () -> ClientPacketHandlers::closeClassAdvancement
        );

        context.setPacketHandled(true);
    }
}
