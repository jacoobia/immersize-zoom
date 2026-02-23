package com.jacoobia.immersivezoom.common.utils;

import net.minecraft.world.item.ItemStack;

import static com.jacoobia.immersivezoom.common.Constants.ZOOM_ITEMS;

/**
 * Utility class for handling operations related to spyglasses, including
 * determining if a player is zooming, locating spyglasses in inventory, and
 * computing magnification levels
 */
public class SpyglassUtils {

    /**
     * Checks if the given ItemStack is a spyglass
     * @param stack the ItemStack to check
     * @return true if the ItemStack is in the immersive_zoom:zoom_items tag, false otherwise
     */
    public static boolean isSpyglass(ItemStack stack) {
        return stack.is(ZOOM_ITEMS);
    }

}
