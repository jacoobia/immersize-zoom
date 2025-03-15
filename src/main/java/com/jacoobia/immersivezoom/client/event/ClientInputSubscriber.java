package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.common.Constants;
import com.jacoobia.immersivezoom.common.item.AttunedSpyglass;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

import static com.jacoobia.immersivezoom.common.Constants.*;
import static com.jacoobia.immersivezoom.common.registry.KeyMapRegistry.ZOOM_TOGGLE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

@SuppressWarnings("DataFlowIssue")
@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientInputSubscriber {

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) return;

        ItemStack stack = player.getUseItem();
        if (!stack.isEmpty() && stack.is(ItemRegistry.ATTUNED_SPYGLASS.get())) {
            float currentZoom = AttunedSpyglass.getZoomLevel(stack);
            double scrollDelta = event.getScrollDeltaY();

            float newZoom = currentZoom;
            float step = player.isCrouching() ?  QUICK_ZOOM_STEP : ZOOM_STEP;

            if (scrollDelta > INTEGER_ZERO) {
                newZoom = Math.min(currentZoom + step, MAX_ZOOM);
            } else if (scrollDelta < INTEGER_ZERO) {
                newZoom = Math.max(currentZoom - step, MIN_ZOOM);
            }

            if (newZoom != currentZoom) {
                AttunedSpyglass.setZoomLevel(stack, newZoom);
            }

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.screen != null) return;

        if (ZOOM_TOGGLE.isDown()) {
            ItemStack spyglassStack = player.getUseItem();

            // Check if the held item is the Attuned Spyglass
            if (!spyglassStack.isEmpty() && spyglassStack.is(ItemRegistry.ATTUNED_SPYGLASS.get())) {
                player.startUsingItem(null);
            }
        }
    }
}

