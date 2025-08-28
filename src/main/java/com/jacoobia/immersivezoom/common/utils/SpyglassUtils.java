package com.jacoobia.immersivezoom.common.utils;

import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.jacoobia.immersivezoom.common.Constants.ZOOM_ITEMS;

/**
 * Utility class for handling operations related to spyglasses, including
 * determining if a player is zooming, locating spyglasses in inventory, and
 * computing magnification levels.
 */
public class SpyglassUtils {

    /**
     * Checks if the player is currently zooming with an attuned spyglass.
     * @return true if the player is using an attuned spyglass, false otherwise.
     */
    public static boolean isZoomingWithAttunedSpyglass() {
        Player player = ClientUtils.getLocalPlayer();
        ItemStack inUse = player.getUseItem();
        return !inUse.isEmpty() && inUse.is(ItemRegistry.ATTUNED_SPYGLASS.get());
    }

    /**
     * Checks if the given ItemStack is a spyglass.
     * @param stack the ItemStack to check.
     * @return true if the ItemStack is a spyglass, false otherwise.
     */
    public static boolean isSpyglass(ItemStack stack) {
        return CollectionUtils.contains(ZOOM_ITEMS, stack.getItem());
    }

}
