package com.jacoobia.immersivezoom.server.events;

import com.jacoobia.immersivezoom.common.container.SpyglassSlot;
import com.jacoobia.immersivezoom.common.data.SpyglassSlotData;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassSlotSyncPacket;
import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import com.jacoobia.immersivezoom.server.utils.MidnightCheck;
import com.jacoobia.immersivezoom.server.utils.ServerUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;
import static com.jacoobia.immersivezoom.common.Constants.SPYGLASS_SLOT_INDEX;

/**
 * An event subscriber for all client sync point related events
 */
@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerSyncPointSubscriber {

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ServerUtils.setHipItem(player, player.getInventory().getItem(SPYGLASS_SLOT_INDEX));
            SpyglassSlot.syncSlotContent(player);
            MidnightCheck.scheduleArrival(player);
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ServerUtils.setHipItem(player, player.getInventory().getItem(SPYGLASS_SLOT_INDEX));
            SpyglassSlot.syncSlotContent(player);
        }
    }

    @SubscribeEvent
    public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SpyglassSlot.syncSlotContent(player);
            if (event.getTo() == Level.OVERWORLD) MidnightCheck.scheduleArrival(player);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getTarget() instanceof ServerPlayer tracked)) return;
        if (!(event.getEntity() instanceof ServerPlayer viewer)) return;

        var data = java.util.Objects.requireNonNullElse(
                tracked.getData(AttachmentRegistry.HIP_SPYGLASS),
                SpyglassSlotData.EMPTY
        );

        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(
                viewer, new SpyglassSlotSyncPacket(tracked.getId(), data)
        );
    }

}
