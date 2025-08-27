package com.jacoobia.immersivezoom.server.events;

import com.jacoobia.immersivezoom.server.SpyglassState;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;
import static com.jacoobia.immersivezoom.server.utils.ServerUtils.getServerState;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerInteractSubscriber {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SpyglassState spyglassState = getServerState(player);
            if (spyglassState.isScoping() && spyglassState.fromSlot()) {   // HOTKEY scoping => block *everything*
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SpyglassState spyglassState = getServerState(player);
            if (spyglassState.isScoping() && spyglassState.fromSlot()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SpyglassState spyglassState = getServerState(player);
            if (spyglassState.isScoping() && spyglassState.fromSlot()) {
                event.setCanceled(true);
            }
        }
    }

}
