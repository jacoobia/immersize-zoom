package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.client.utils.LocalZoomState;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassScopePacket;
import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.jacoobia.immersivezoom.common.Constants.*;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientTickSubscriber {

    private static boolean wasScoping = false;

    // Pre, so the zoom state is fresh before the game ticks the FOV this tick
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        LocalPlayer player = ClientUtils.getLocalPlayerCast();
        if (player == null) {
            LocalZoomState.reset();
            return;
        }

        LocalZoomState.update(player);
        ClientInputSubscriber.tickZoomSync();

        boolean scoping = LocalZoomState.isHotkeyScoping();
        if (scoping != wasScoping) {
            if (scoping) {
                PacketDistributor.sendToServer(SpyglassScopePacket.start());
            } else {
                PacketDistributor.sendToServer(SpyglassScopePacket.stop());
            }
            wasScoping = scoping;
        }
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        wasScoping = false;
        LocalZoomState.reset();
        ClientInputSubscriber.resetZoomSync();
    }
}
