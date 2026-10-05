package com.cyberspectraa.cyberclasses.network;

import com.cyberspectraa.cyberclasses.CyberClasses;
import com.cyberspectraa.cyberclasses.network.packet.CloseClassCreatorPacket;
import com.cyberspectraa.cyberclasses.network.packet.OpenClassCreatorPacket;
import com.cyberspectraa.cyberclasses.network.packet.SubmitClassPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class CyberClassesNetwork {
    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL =
        NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CyberClasses.MOD_ID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
        );

    private static int messageId;

    private CyberClassesNetwork() {
    }

    public static void init() {
        CHANNEL.messageBuilder(
                OpenClassCreatorPacket.class,
                messageId++,
                NetworkDirection.PLAY_TO_CLIENT
            )
            .encoder(OpenClassCreatorPacket::encode)
            .decoder(OpenClassCreatorPacket::decode)
            .consumerMainThread(OpenClassCreatorPacket::handle)
            .add();

        CHANNEL.messageBuilder(
                SubmitClassPacket.class,
                messageId++,
                NetworkDirection.PLAY_TO_SERVER
            )
            .encoder(SubmitClassPacket::encode)
            .decoder(SubmitClassPacket::decode)
            .consumerMainThread(SubmitClassPacket::handle)
            .add();

        CHANNEL.messageBuilder(
                CloseClassCreatorPacket.class,
                messageId++,
                NetworkDirection.PLAY_TO_CLIENT
            )
            .encoder(CloseClassCreatorPacket::encode)
            .decoder(CloseClassCreatorPacket::decode)
            .consumerMainThread(CloseClassCreatorPacket::handle)
            .add();
    }

    public static void sendToPlayer(
        ServerPlayer player,
        Object packet
    ) {
        CHANNEL.send(
            PacketDistributor.PLAYER.with(() -> player),
            packet
        );
    }

    public static void sendToServer(Object packet) {
        CHANNEL.sendToServer(packet);
    }
}
