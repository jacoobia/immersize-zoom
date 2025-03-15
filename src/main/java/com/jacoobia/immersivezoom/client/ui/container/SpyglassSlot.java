package com.jacoobia.immersivezoom.client.ui.container;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

import static com.jacoobia.immersivezoom.common.Constants.EMPTY_SLOT_SPYGLASS;
import static net.minecraft.world.inventory.InventoryMenu.BLOCK_ATLAS;

public class SpyglassSlot extends Slot {

    public SpyglassSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(BLOCK_ATLAS, EMPTY_SLOT_SPYGLASS);
    }

}
