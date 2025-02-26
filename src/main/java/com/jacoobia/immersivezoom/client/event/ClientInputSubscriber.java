package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.common.Constants;
import com.jacoobia.immersivezoom.common.item.AttunedSpyglass;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientInputSubscriber {

    private static final float MAX_ZOOM = 0.1f;
    private static final float MIN_ZOOM = 1.0f;
    private static final float ZOOM_STEP = 0.1f;

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Player player = Minecraft.getInstance().player;
        if(player == null) return;

        ItemStack stack = player.getUseItem();
        if (!stack.isEmpty() && stack.getItem() instanceof AttunedSpyglass) {
            float currentZoom = AttunedSpyglass.getZoomLevel(stack);
            double scrollDelta = event.getScrollDeltaY();

            float newZoom = currentZoom;
            if (scrollDelta > 0) {
                newZoom = Math.max(currentZoom - ZOOM_STEP, MAX_ZOOM); // Zoom in
            } else if (scrollDelta < 0) {
                newZoom = Math.min(currentZoom + ZOOM_STEP, MIN_ZOOM); // Zoom out
            }

            AttunedSpyglass.setZoomLevel(stack, newZoom);
//            ZoomShaderHandler.setZoomFactor(newZoom);

            event.setCanceled(true);
        }
    }

}
