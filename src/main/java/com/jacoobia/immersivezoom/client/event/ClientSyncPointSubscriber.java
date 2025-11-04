package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

/**
 * An event subscriber for the points where the client needs to send its settings to the server
 */
@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientSyncPointSubscriber {

    @SubscribeEvent
    public static void onLoggingIn(ClientPlayerNetworkEvent.LoggingIn event) {
        ClientUtils.sendBeltPlacement();
    }

    @SubscribeEvent
    public static void onClone(ClientPlayerNetworkEvent.Clone event) {
        ClientUtils.sendBeltPlacement();
    }

}
