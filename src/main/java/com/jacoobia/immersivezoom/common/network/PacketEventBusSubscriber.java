package com.jacoobia.immersivezoom.common.network;

import com.jacoobia.immersivezoom.server.network.handlers.ServerSpyglassScopePacketHandler;
import com.jacoobia.immersivezoom.server.network.handlers.ServerSpyglassZoomPacketHandler;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassScopePacket;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassZoomPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class PacketEventBusSubscriber {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                SpyglassScopePacket.TYPE,
                SpyglassScopePacket.STREAM_CODEC,
                ServerSpyglassScopePacketHandler.get()::handle
        );

        registrar.playToServer(
                SpyglassZoomPacket.TYPE,
                SpyglassZoomPacket.STREAM_CODEC,
                new ServerSpyglassZoomPacketHandler()::handle
        );

    }

}
