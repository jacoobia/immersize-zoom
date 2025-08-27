package com.jacoobia.immersivezoom.common.utils;

import com.jacoobia.immersivezoom.client.registry.KeyMapRegistry;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.jacoobia.immersivezoom.client.registry.KeyMapRegistry.ZOOM_KEYMAPPING;
import static com.jacoobia.immersivezoom.common.Constants.*;

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
     * Retrieves the currently active spyglass being used for zooming.
     * @return the ItemStack of the active spyglass, or an empty ItemStack if none is active.
     */
    public static ItemStack getActiveZoomingSpyglass() {
        LocalPlayer player = ClientUtils.getLocalPlayerCast();
        if (player == null) return ItemStack.EMPTY;

        if (ZOOM_KEYMAPPING.isDown()) {
            ItemStack slot = player.getInventory().getItem(SPYGLASS_SLOT_INDEX);
            if (isSpyglass(slot)) return slot;
        }

        ItemStack main = player.getMainHandItem();
        if (isSpyglass(main)) return main;

        ItemStack off = player.getOffhandItem();
        if (isSpyglass(off)) return off;

        return ItemStack.EMPTY;
    }

    /**
     * Checks if the given ItemStack is a spyglass.
     * @param stack the ItemStack to check.
     * @return true if the ItemStack is a spyglass, false otherwise.
     */
    public static boolean isSpyglass(ItemStack stack) {
        return CollectionUtils.contains(ZOOM_ITEMS, stack.getItem());
    }

    /**
     * Retrieves the spyglass from a special inventory slot, if present.
     * @return the ItemStack of the spyglass in the special slot, or an empty ItemStack if none is found.
     */
    public static ItemStack getSpyglassInSpecialSlot() {
        Player player = ClientUtils.getLocalPlayer();
        ItemStack slot = player.getInventory().getItem(SPYGLASS_SLOT_INDEX);
        return isSpyglass(slot) ? slot : ItemStack.EMPTY;
    }


    public static ItemStack resolveSpyglassFromSpecialSlot(Player player) {
        ItemStack slot = player.getInventory().getItem(SPYGLASS_SLOT_INDEX);
        return isSpyglass(slot) ? slot : ItemStack.EMPTY;
    }

    /**
     * Resolves the active spyglass from the player's main hand, offhand, or a special inventory slot.
     * @param player the player whose inventory to check.
     * @return the ItemStack of the active spyglass, or an empty ItemStack if none is found.
     */
    public static ItemStack resolveActiveSpyglass(Player player) {
        ItemStack main = player.getMainHandItem();
        if (isSpyglass(main)) return main;
        ItemStack off = player.getOffhandItem();
        if (isSpyglass(off)) return off;
        return resolveSpyglassFromSpecialSlot(player);
    }

}
