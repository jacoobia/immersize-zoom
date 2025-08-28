package com.jacoobia.immersivezoom.server.events;

import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import com.jacoobia.immersivezoom.common.utils.DataUtils;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;
import static com.jacoobia.immersivezoom.common.registry.DataComponentRegistry.TESTER;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerInteractSubscriber {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() instanceof ServerPlayer) {
            if(event.getItemStack().is(ItemRegistry.ATTUNED_AMETHYST_SHARD)) {
                int total = DataUtils.getIntOrDefault(event.getItemStack(), TESTER, 0);
                total += 1;
                event.getItemStack().set(TESTER, (int) total);
            }
        }
    }

}
