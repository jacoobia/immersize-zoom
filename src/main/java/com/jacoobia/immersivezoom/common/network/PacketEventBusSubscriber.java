package com.jacoobia.immersivezoom.common.network;

import com.jacoobia.immersivezoom.common.network.handlers.ClientSpyglassPacketHandler;
import com.jacoobia.immersivezoom.common.network.handlers.ServerSpyglassPacketHandler;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassScopePacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class PacketEventBusSubscriber {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playBidirectional(
                SpyglassScopePacket.TYPE,
                SpyglassScopePacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ServerSpyglassPacketHandler.get()::handle,
                        ClientSpyglassPacketHandler.get()::handle
                )
        );

    }

}
