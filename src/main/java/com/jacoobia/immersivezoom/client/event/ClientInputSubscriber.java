package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.client.utils.LocalZoomState;
import com.jacoobia.immersivezoom.common.item.AttunedSpyglass;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassZoomPacket;
import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import com.jacoobia.immersivezoom.common.utils.DataUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.jacoobia.immersivezoom.common.Constants.ATTUNED_ZOOM_DEFAULT;
import static com.jacoobia.immersivezoom.common.Constants.ATTUNED_ZOOM_MAX;
import static com.jacoobia.immersivezoom.common.Constants.ATTUNED_ZOOM_MIN;
import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;
import static com.jacoobia.immersivezoom.common.Constants.QUICK_ZOOM_STEP;
import static com.jacoobia.immersivezoom.common.Constants.ZOOM_STEP;
import static com.jacoobia.immersivezoom.common.registry.DataComponentRegistry.ZOOM_FACTOR;
import static com.jacoobia.immersivezoom.common.utils.NumberUtils.INT_ZERO;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientInputSubscriber {

    // Scroll wheels and trackpads can fire many notches a second; send the latest value at most this often
    private static final int ZOOM_SYNC_INTERVAL_TICKS = 5;

    private static SpyglassZoomPacket pendingZoom = null;
    private static int ticksSinceZoomSync = ZOOM_SYNC_INTERVAL_TICKS;

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        LocalPlayer player = ClientUtils.getLocalPlayerCast();
        if (player == null || ClientUtils.isThirdPerson()) return;

        ItemStack activeSpyglassStack = LocalZoomState.activeSpyglass();
        if (!(activeSpyglassStack.getItem() instanceof AttunedSpyglass)) return;

        event.setCanceled(true);

        double deltaY = event.getScrollDeltaY();
        if (deltaY == INT_ZERO) return;

        float step = player.isShiftKeyDown() ? QUICK_ZOOM_STEP : ZOOM_STEP;
        float current = DataUtils.getFloatOrDefault(activeSpyglassStack, ZOOM_FACTOR, ATTUNED_ZOOM_DEFAULT);
        float next = Mth.clamp(deltaY > 0 ? current * step : current / step, ATTUNED_ZOOM_MIN, ATTUNED_ZOOM_MAX);

        // Update the client copy straight away; the server copy catches up in tickZoomSync
        activeSpyglassStack.set(ZOOM_FACTOR, next);
        pendingZoom = SpyglassZoomPacket.create(LocalZoomState.hand(), next);
    }

    /**
     * Sends the latest scroll zoom to the server, at most once every ZOOM_SYNC_INTERVAL_TICKS.
     * Called every client tick.
     */
    public static void tickZoomSync() {
        if (ticksSinceZoomSync < ZOOM_SYNC_INTERVAL_TICKS) ticksSinceZoomSync++;
        if (pendingZoom == null || ticksSinceZoomSync < ZOOM_SYNC_INTERVAL_TICKS) return;

        PacketDistributor.sendToServer(pendingZoom);
        pendingZoom = null;
        ticksSinceZoomSync = 0;
    }

    public static void resetZoomSync() {
        pendingZoom = null;
        ticksSinceZoomSync = ZOOM_SYNC_INTERVAL_TICKS;
    }
}
