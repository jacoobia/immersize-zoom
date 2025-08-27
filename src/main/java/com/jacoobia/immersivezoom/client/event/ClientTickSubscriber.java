package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.client.ClientSpyglassState;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassScopePacket;
import com.jacoobia.immersivezoom.common.utils.ClientUtils;
import com.jacoobia.immersivezoom.common.utils.SpyglassUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.jacoobia.immersivezoom.client.registry.KeyMapRegistry.ZOOM_KEYMAPPING;
import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;
import static com.jacoobia.immersivezoom.common.Constants.SPYGLASS_SLOT_INDEX;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientTickSubscriber {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        LocalPlayer localPlayer = ClientUtils.getLocalPlayerCast();
        if (localPlayer == null) return;

        boolean isFront = ClientUtils.isThirdPersonFront();
        boolean zoomKeyIsDown = ZOOM_KEYMAPPING.isDown();

        if (ClientSpyglassState.isLocalHeld() && isFront) {
            ClientSpyglassState.setLocalHeld(false);
            PacketDistributor.sendToServer(SpyglassScopePacket.stop());
            localPlayer.playSound(SoundEvents.SPYGLASS_STOP_USING, 1.0F, 1.0F);
            return;
        }

        if (zoomKeyIsDown && !ClientSpyglassState.isLocalHeld()) {
            if (isFront) return;

            ClientSpyglassState.setLocalHeld(true);
            boolean usingSpecialSlot;
            ItemStack activeSpyglassStack = ClientSpyglassState.getActiveSpyglassClient();
            if (activeSpyglassStack.isEmpty()) {
                ItemStack slot = localPlayer.getInventory().getItem(SPYGLASS_SLOT_INDEX);
                if (!SpyglassUtils.isSpyglass(slot)) {
                    ClientSpyglassState.setLocalHeld(false);
                    return;
                }
                usingSpecialSlot = true;
            } else {
                usingSpecialSlot = !(activeSpyglassStack == localPlayer.getMainHandItem()
                        || activeSpyglassStack == localPlayer.getOffhandItem());
            }

            PacketDistributor.sendToServer(SpyglassScopePacket.start(usingSpecialSlot));
            localPlayer.playSound(SoundEvents.SPYGLASS_USE, 1.0F, 1.0F);
            ClientSpyglassState.seedLocalZoom();
        }
        else if (!zoomKeyIsDown && ClientSpyglassState.isLocalHeld()) {
            ClientSpyglassState.setLocalHeld(false);
            if (ClientSpyglassState.isAttunedActive()) {
                PacketDistributor.sendToServer(SpyglassScopePacket.setFinal(ClientSpyglassState.getLocalZoom()));
            }
            PacketDistributor.sendToServer(SpyglassScopePacket.stop());
            localPlayer.playSound(SoundEvents.SPYGLASS_STOP_USING, 1.0F, 1.0F);
        }
    }

}

