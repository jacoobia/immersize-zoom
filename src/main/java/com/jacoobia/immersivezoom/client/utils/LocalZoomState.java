package com.jacoobia.immersivezoom.client.utils;

import com.jacoobia.immersivezoom.common.utils.SpyglassUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import static com.jacoobia.immersivezoom.client.registry.KeyMapRegistry.ZOOM_KEYMAPPING;
import static com.jacoobia.immersivezoom.common.Constants.SPYGLASS_SLOT_INDEX;

/**
 * The local player's zoom state, worked out once per client tick.
 * FOV, HUD, isScoping, scroll input and the hotkey packets all read this, so they always agree.
 * Camera type isn't part of it: it can change between frames, so callers check it themselves.
 */
public final class LocalZoomState {

    private static boolean zooming = false;
    private static InteractionHand hand = null;
    private static boolean hotkeyScoping = false;

    private LocalZoomState() {}

    public static void update(LocalPlayer player) {
        hotkeyScoping = ZOOM_KEYMAPPING.isDown()
                && SpyglassUtils.isSpyglass(player.getInventory().getItem(SPYGLASS_SLOT_INDEX));

        // A spyglass in use in hand takes priority over the hotkey
        if (player.isUsingItem() && SpyglassUtils.isSpyglass(player.getUseItem())) {
            zooming = true;
            hand = player.getUsedItemHand();
        } else {
            zooming = hotkeyScoping;
            hand = null;
        }
    }

    public static void reset() {
        zooming = false;
        hand = null;
        hotkeyScoping = false;
    }

    /**
     * Looks the stack up each call rather than caching it: a server inventory sync replaces the
     * stack object, and a cached one would go stale until the next tick.
     * @return the spyglass being looked through: the one in use in hand, else the slot one while
     * the hotkey is held, else EMPTY
     */
    public static ItemStack activeSpyglass() {
        LocalPlayer player = ClientUtils.getLocalPlayerCast();
        if (!zooming || player == null) return ItemStack.EMPTY;
        return hand == null
                ? player.getInventory().getItem(SPYGLASS_SLOT_INDEX)
                : player.getItemInHand(hand);
    }

    public static boolean isZooming() {
        return zooming;
    }

    /**
     * @return the hand holding the active spyglass, or null when zooming from the spyglass slot
     */
    public static InteractionHand hand() {
        return hand;
    }

    public static boolean isHotkeyScoping() {
        return hotkeyScoping;
    }

}
