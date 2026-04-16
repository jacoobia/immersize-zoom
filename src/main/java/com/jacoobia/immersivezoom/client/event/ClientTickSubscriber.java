package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.client.config.ClientConfig;
import com.jacoobia.immersivezoom.client.render.BeltPhysics;
import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import com.jacoobia.immersivezoom.client.utils.LocalZoomState;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassScopePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientTickSubscriber {

    private static boolean wasScoping = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        LocalPlayer player = ClientUtils.getLocalPlayerCast();
        if (player == null) {
            LocalZoomState.reset();
            return;
        }

        LocalZoomState.update(player);
        ClientInputSubscriber.tickZoomSync();
        if (ClientConfig.BELT_SWAY.get() && !Minecraft.getInstance().isPaused()) BeltPhysics.tickAll(player.clientLevel);

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
