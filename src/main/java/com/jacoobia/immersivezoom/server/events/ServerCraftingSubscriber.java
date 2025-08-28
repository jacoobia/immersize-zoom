package com.jacoobia.immersivezoom.server.events;

import com.jacoobia.immersivezoom.common.utils.ItemUtils;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;
import static com.jacoobia.immersivezoom.common.registry.ItemRegistry.*;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerCraftingSubscriber {

    @SubscribeEvent
    public static void onCraft(PlayerEvent.ItemCraftedEvent event) {
        ItemStack result = event.getCrafting();
        if (result.isEmpty()) return;
        if (result.is(ATTUNED_SPYGLASS) || result.is(ATTUNED_AMETHYST_SHARD)) {
            ItemUtils.setItemId(result);
        }
    }

}
