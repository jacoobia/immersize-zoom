package com.jacoobia.immersivezoom.common.registry;

import com.jacoobia.immersivezoom.common.item.AttunedAmethystShard;
import com.jacoobia.immersivezoom.common.item.AttunedSpyglass;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

public class ItemRegistry {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

    public static void init(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static final DeferredItem<Item> ATTUNED_AMETHYST_SHARD = ITEMS.register(
            "attuned_amethyst_shard",
            AttunedAmethystShard::new
    );

    public static final DeferredItem<Item> ATTUNED_SPYGLASS = ITEMS.register(
            "attuned_spyglass",
            AttunedSpyglass::new
    );

}
