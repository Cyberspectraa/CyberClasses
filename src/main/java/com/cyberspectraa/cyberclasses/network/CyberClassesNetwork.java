package com.cyberspectraa.cyberclasses.network;

import com.cyberspectraa.cyberclasses.CyberClasses;
import com.cyberspectraa.cyberclasses.network.packet.CloseClassAdvancementPacket;
import com.cyberspectraa.cyberclasses.network.packet.CloseClassCreatorPacket;
import com.cyberspectraa.cyberclasses.network.packet.OpenClassAdvancementPacket;
import com.cyberspectraa.cyberclasses.network.packet.OpenClassCreatorPacket;
import com.cyberspectraa.cyberclasses.network.packet.SubmitClassAdvancementPacket;
import com.cyberspectraa.cyberclasses.network.packet.SubmitClassPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class CyberClassesNetwork {
    private static final String PROTOCOL = "2";

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

        CHANNEL.messageBuilder(
                OpenClassAdvancementPacket.class,
                messageId++,
                NetworkDirection.PLAY_TO_CLIENT
            )
            .encoder(OpenClassAdvancementPacket::encode)
            .decoder(OpenClassAdvancementPacket::decode)
            .consumerMainThread(OpenClassAdvancementPacket::handle)
            .add();

        CHANNEL.messageBuilder(
                SubmitClassAdvancementPacket.class,
                messageId++,
                NetworkDirection.PLAY_TO_SERVER
            )
            .encoder(SubmitClassAdvancementPacket::encode)
            .decoder(SubmitClassAdvancementPacket::decode)
            .consumerMainThread(SubmitClassAdvancementPacket::handle)
            .add();

        CHANNEL.messageBuilder(
                CloseClassAdvancementPacket.class,
                messageId++,
                NetworkDirection.PLAY_TO_CLIENT
            )
            .encoder(CloseClassAdvancementPacket::encode)
            .decoder(CloseClassAdvancementPacket::decode)
            .consumerMainThread(CloseClassAdvancementPacket::handle)
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
