package com.jacoobia.immersivezoom.client.utils;

import com.jacoobia.immersivezoom.common.utils.SpyglassUtils;
import lombok.Getter;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import static com.jacoobia.immersivezoom.client.registry.KeyMapRegistry.ZOOM_KEYMAPPING;
import static com.jacoobia.immersivezoom.common.Constants.SPYGLASS_SLOT_INDEX;

public final class LocalZoomState {

    @Getter
    private static boolean zooming = false;

    @Getter
    private static InteractionHand hand = null;

    @Getter
    private static boolean hotkeyScoping = false;

    private LocalZoomState() {}

    public static void update(LocalPlayer player) {
        hotkeyScoping = ZOOM_KEYMAPPING.isDown()
                && SpyglassUtils.isSpyglass(player.getInventory().getItem(SPYGLASS_SLOT_INDEX));

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
     * Looks up the spyglass stack that is in use
     * checks hand then slot otherwise null
     * @return the spyglass being used
     */
    public static ItemStack activeSpyglass() {
        LocalPlayer player = ClientUtils.getLocalPlayerCast();
        if (!zooming || player == null) return ItemStack.EMPTY;
        return hand == null
                ? player.getInventory().getItem(SPYGLASS_SLOT_INDEX)
                : player.getItemInHand(hand);
    }

}
