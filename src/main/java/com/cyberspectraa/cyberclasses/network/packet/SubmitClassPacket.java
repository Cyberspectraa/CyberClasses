package com.cyberspectraa.cyberclasses.network.packet;

import com.cyberspectraa.cyberclasses.classdata.ClassCreationManager;
import com.cyberspectraa.cyberclasses.classdata.PlayerClass;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SubmitClassPacket(String classId) {
    public static void encode(
        SubmitClassPacket packet,
        FriendlyByteBuf buffer
    ) {
        buffer.writeUtf(packet.classId(), 32);
    }

    public static SubmitClassPacket decode(
        FriendlyByteBuf buffer
    ) {
        return new SubmitClassPacket(
            buffer.readUtf(32)
        );
    }

    public static void handle(
        SubmitClassPacket packet,
        Supplier<NetworkEvent.Context> contextSupplier
    ) {
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer player = context.getSender();

        if (player != null) {
            PlayerClass.byId(packet.classId())
                .ifPresent(playerClass ->
                    ClassCreationManager.complete(
                        player,
                        playerClass
                    )
                );
        }

        context.setPacketHandled(true);
    }
}
