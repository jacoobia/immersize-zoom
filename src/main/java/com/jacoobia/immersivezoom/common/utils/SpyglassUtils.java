package com.jacoobia.immersivezoom.common.utils;

import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Utility class for handling operations related to spyglasses, including
 * determining if a player is zooming, locating spyglasses in inventory, and
 * computing magnification levels
 */
public class SpyglassUtils {

    /**
     * Checks if the given ItemStack is a spyglass
     * @param stack the ItemStack to check
     * @return true if the ItemStack is a spyglass, false otherwise
     */
    public static boolean isSpyglass(ItemStack stack) {
        return stack.is(Items.SPYGLASS) || stack.is(ItemRegistry.ATTUNED_SPYGLASS);
    }

}
