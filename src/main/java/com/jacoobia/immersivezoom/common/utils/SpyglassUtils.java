package com.jacoobia.immersivezoom.common.utils;

import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import net.minecraft.world.entity.player.Player;
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

    /**
     * Gets the item in the players spyglass slot
     * @param player the player
     * @return the item in the player's spyglass slot, or EMPTY
     */
    public static ItemStack getSlotItem(Player player) {
        return player.getData(AttachmentRegistry.SPYGLASS_SLOT);
    }

}
