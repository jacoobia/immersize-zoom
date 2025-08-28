package com.jacoobia.immersivezoom.server.events;

import com.jacoobia.immersivezoom.common.utils.ItemUtils;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;
import static com.jacoobia.immersivezoom.common.registry.ItemRegistry.ATTUNED_AMETHYST_SHARD;
import static com.jacoobia.immersivezoom.common.registry.ItemRegistry.ATTUNED_SPYGLASS;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerItemPickupSubscriber {

    @SubscribeEvent
    public static void onItemPickup(ItemEntityPickupEvent.Post event) {
        ItemStack stack = event.getItemEntity().getItem();
        if (stack.is(ATTUNED_SPYGLASS) || stack.is(ATTUNED_AMETHYST_SHARD)) {
            ItemUtils.setItemId(stack);
        }
    }

}
