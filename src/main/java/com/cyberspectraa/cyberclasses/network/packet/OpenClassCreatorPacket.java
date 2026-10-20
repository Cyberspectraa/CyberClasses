package com.cyberspectraa.cyberclasses.network.packet;

import com.cyberspectraa.cyberclasses.client.ClientPacketHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record OpenClassCreatorPacket(boolean guildMode) {
    public OpenClassCreatorPacket() {
        this(false);
    }
    public static void encode(
        OpenClassCreatorPacket packet,
        FriendlyByteBuf buffer
    ) {
        buffer.writeBoolean(packet.guildMode());
    }

    public static OpenClassCreatorPacket decode(
        FriendlyByteBuf buffer
    ) {
        return new OpenClassCreatorPacket(buffer.readBoolean());
    }

    public static void handle(
        OpenClassCreatorPacket packet,
        Supplier<NetworkEvent.Context> contextSupplier
    ) {
        NetworkEvent.Context context = contextSupplier.get();

        DistExecutor.unsafeRunWhenOn(
            Dist.CLIENT,
            () -> () -> ClientPacketHandlers.openClassCreator(packet.guildMode())
        );

        context.setPacketHandled(true);
    }
}
