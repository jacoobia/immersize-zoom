package com.jacoobia.immersivezoom.client.utils;

import com.jacoobia.immersivezoom.common.data.SpyglassSlotData;
import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import com.jacoobia.immersivezoom.common.utils.SpyglassType;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

import static com.jacoobia.immersivezoom.client.registry.KeyMapRegistry.ZOOM_KEYMAPPING;

public class ClientUtils {

    /**
     * Get the local player from the Minecraft instance
     * @return The local player
     */
    public static Player getLocalPlayer() {
        return Minecraft.getInstance().player;
    }

    /**
     * Get the local player cast as LocalPlayer
     * @return The local player as LocalPlayer
     */
    public static LocalPlayer getLocalPlayerCast() {
        Player player = getLocalPlayer();
        return (LocalPlayer) player;
    }

    /**
     * Checks if the local player is currently zooming with an attuned spyglass
     * @return true if the player is using an attuned spyglass, false otherwise
     */
    public static boolean isZoomingWithAttunedSpyglass() {
        Player player = getLocalPlayer();
        return player != null && player.getUseItem().is(ItemRegistry.ATTUNED_SPYGLASS.get());
    }

    /**
     * Check if the player is in third person front view
     * @return true if the player is in third person front view
     */
    public static boolean isThirdPerson() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.options.getCameraType() != CameraType.FIRST_PERSON;
    }

    public static boolean isHotkeyScoping(Player player) {
        SpyglassSlotData data = player.getData(AttachmentRegistry.HIP_SPYGLASS);
        if (data.spyglassType() == SpyglassType.NONE) return false;
        return player.isLocalPlayer() ? ZOOM_KEYMAPPING.isDown() : data.scoping();
    }


}
