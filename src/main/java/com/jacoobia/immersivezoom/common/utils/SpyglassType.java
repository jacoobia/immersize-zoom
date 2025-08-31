package com.jacoobia.immersivezoom.common.utils;

import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import net.minecraft.world.item.ItemStack;

public enum SpyglassType {
    VANILLA,
    ATTUNED,
    NONE;

    public static SpyglassType fromItemStack(ItemStack stack) {
        if (stack.isEmpty()) {
            return NONE;
        }
        return stack.is(ItemRegistry.ATTUNED_SPYGLASS) ? ATTUNED : VANILLA;
    }

}
