package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.common.network.packet.SpyglassScopePacket;
import com.jacoobia.immersivezoom.common.utils.ClientUtils;
import com.jacoobia.immersivezoom.common.utils.SpyglassUtils;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.jacoobia.immersivezoom.client.registry.KeyMapRegistry.ZOOM_KEYMAPPING;
import static com.jacoobia.immersivezoom.common.Constants.*;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientTickSubscriber {

    private static boolean wasScoping = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        LocalPlayer player = ClientUtils.getLocalPlayerCast();
        if (player == null) return;

        boolean isFront = ClientUtils.isThirdPersonFront();
        boolean keyDown = ZOOM_KEYMAPPING.isDown();

        boolean hasSpyglass =
                SpyglassUtils.isSpyglass(player.getUseItem())
                        || SpyglassUtils.isSpyglass(player.getInventory().getItem(SPYGLASS_SLOT_INDEX));

        boolean wantsScope = keyDown && !isFront && hasSpyglass;

        if (wantsScope != wasScoping) {
            if (wantsScope) {
                PacketDistributor.sendToServer(SpyglassScopePacket.start());
            } else {
                PacketDistributor.sendToServer(SpyglassScopePacket.stop());
            }
            wasScoping = wantsScope;
        }
    }
}


