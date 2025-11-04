package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import com.jacoobia.immersivezoom.client.utils.LocalZoomState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import static com.jacoobia.immersivezoom.common.Constants.*;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientHudSubscriber {

    /**
     * Used to disable the HUD and other overlays when scoping with the hotkey
     * @param event The RenderGuiLayerEvent.Pre event
     */
    @SubscribeEvent
    public static void onOverlayPre(RenderGuiLayerEvent.Pre event) {
        if (!LocalZoomState.isZooming() || ClientUtils.isThirdPerson()) return;

        if (!event.getName().equals(VanillaGuiLayers.CAMERA_OVERLAYS))
            event.setCanceled(true);
    }

}
