package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.common.Constants;
import com.jacoobia.immersivezoom.common.item.AttunedSpyglass;
import com.jacoobia.immersivezoom.common.utils.CollectionUtils;
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
        Player player = Minecraft.getInstance().player;
        if(player == null) return;

        ItemStack stack = player.getUseItem();
        if (!stack.isEmpty() && stack.getItem() instanceof AttunedSpyglass) {
            float currentZoom = AttunedSpyglass.getZoomLevel(stack);
            double scrollDelta = event.getScrollDeltaY();

            float newZoom = currentZoom;
            if (scrollDelta > INTEGER_ZERO) {
                newZoom = Math.max(currentZoom - ZOOM_STEP, MAX_ZOOM);
            } else if (scrollDelta < INTEGER_ZERO) {
                newZoom = Math.min(currentZoom + ZOOM_STEP, MIN_ZOOM);
            }

            AttunedSpyglass.setZoomLevel(stack, newZoom);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Player player = Minecraft.getInstance().player;
        if (player == null || Minecraft.getInstance().screen != null) return;

        if (ZOOM_TOGGLE.isDown()) {
            ItemStack spyglassStack = player.getInventory().getItem(SPYGLASS_SLOT_INDEX);
            if (spyglassStack.isEmpty() || !CollectionUtils.contains(ZOOM_ITEMS, spyglassStack.getItem())) return;
            player.startUsingItem(null);
        }
    }

}
