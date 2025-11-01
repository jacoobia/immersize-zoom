package com.jacoobia.immersivezoom.server.events;

import com.jacoobia.immersivezoom.client.ui.container.SpyglassSlot;
import com.jacoobia.immersivezoom.common.data.SpyglassSlotData;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassSlotSyncPacket;
import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

/**
 * An event subscriber for all client sync point related events
 */
@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerSyncPointSubscriber {

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SpyglassSlot.syncSlotContent(player);
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SpyglassSlot.syncSlotContent(player);
        }
    }

    @SubscribeEvent
    public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SpyglassSlot.syncSlotContent(player);
        }
    }

    @SubscribeEvent
    public static void onJoinWorld(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SpyglassSlot.syncSlotContent(player);
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
