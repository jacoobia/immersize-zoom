package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.common.item.AttunedSpyglass;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassZoomPacket;
import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import com.jacoobia.immersivezoom.common.utils.DataUtils;
import com.jacoobia.immersivezoom.common.utils.ItemUtils;
import com.jacoobia.immersivezoom.common.utils.SpyglassUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.UUID;

import static com.jacoobia.immersivezoom.client.registry.KeyMapRegistry.ZOOM_KEYMAPPING;
import static com.jacoobia.immersivezoom.common.Constants.ATTUNED_ZOOM_DEFAULT;
import static com.jacoobia.immersivezoom.common.Constants.ATTUNED_ZOOM_MAX;
import static com.jacoobia.immersivezoom.common.Constants.ATTUNED_ZOOM_MIN;
import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;
import static com.jacoobia.immersivezoom.common.Constants.QUICK_ZOOM_STEP;
import static com.jacoobia.immersivezoom.common.Constants.SPYGLASS_SLOT_INDEX;
import static com.jacoobia.immersivezoom.common.Constants.ZOOM_STEP;
import static com.jacoobia.immersivezoom.common.registry.DataComponentRegistry.ZOOM_FACTOR;
import static com.jacoobia.immersivezoom.common.utils.NumberUtils.INT_ZERO;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientInputSubscriber {

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        LocalPlayer player = ClientUtils.getLocalPlayerCast();
        if (player == null) return;
        if (ClientUtils.isThirdPersonFront()) return;

        boolean inHandScoping = player.isUsingItem() && SpyglassUtils.isSpyglass(player.getUseItem());
        boolean hotkeyScoping = ZOOM_KEYMAPPING.isDown()
                && SpyglassUtils.isSpyglass(player.getInventory().getItem(SPYGLASS_SLOT_INDEX));

        if (!(inHandScoping || hotkeyScoping)) return;

        ItemStack activeSpyglassStack;
        if (inHandScoping) {
            activeSpyglassStack = player.getUseItem();
        } else {
            activeSpyglassStack = player.getInventory().getItem(SPYGLASS_SLOT_INDEX);
        }
        if (activeSpyglassStack.isEmpty() || !(activeSpyglassStack.getItem() instanceof AttunedSpyglass)) return;

        event.setCanceled(true);

        double deltaY = event.getScrollDeltaY();
        if (deltaY == INT_ZERO) return;

        float step = player.isShiftKeyDown() ? QUICK_ZOOM_STEP : ZOOM_STEP;
        float current = DataUtils.getFloatOrDefault(activeSpyglassStack, ZOOM_FACTOR, ATTUNED_ZOOM_DEFAULT);
        float next = Mth.clamp(current + Math.signum((float) deltaY) * step, ATTUNED_ZOOM_MIN, ATTUNED_ZOOM_MAX);

        activeSpyglassStack.set(ZOOM_FACTOR, next);

        UUID stackId = ItemUtils.getStackId(activeSpyglassStack);
        PacketDistributor.sendToServer(SpyglassZoomPacket.create(stackId, next));
    }
}


