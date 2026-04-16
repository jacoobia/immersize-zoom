package com.jacoobia.immersivezoom.common.network;

import com.jacoobia.immersivezoom.client.network.handlers.ClientSpyglassSlotSyncPacketHandler;
import com.jacoobia.immersivezoom.common.network.packet.BeltPlacementPacket;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassScopePacket;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassSlotSyncPacket;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassZoomPacket;
import com.jacoobia.immersivezoom.server.network.handlers.ServerBeltPlacementPacketHandler;
import com.jacoobia.immersivezoom.server.network.handlers.ServerSpyglassScopePacketHandler;
import com.jacoobia.immersivezoom.server.network.handlers.ServerSpyglassZoomPacketHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class PacketEventBusSubscriber {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("3");

        registrar.playToServer(
                BeltPlacementPacket.TYPE,
                BeltPlacementPacket.STREAM_CODEC,
                ServerBeltPlacementPacketHandler.get()::handle
        );

        registrar.playToServer(
                SpyglassScopePacket.TYPE,
                SpyglassScopePacket.STREAM_CODEC,
                ServerSpyglassScopePacketHandler.get()::handle
        );

        registrar.playToServer(
                SpyglassZoomPacket.TYPE,
                SpyglassZoomPacket.STREAM_CODEC,
                ServerSpyglassZoomPacketHandler.get()::handle
        );

        registrar.playToClient(
                SpyglassSlotSyncPacket.TYPE,
                SpyglassSlotSyncPacket.STREAM_CODEC,
                (packet, context) -> ClientSpyglassSlotSyncPacketHandler.get().handle(packet, context)
        );

    }

}
