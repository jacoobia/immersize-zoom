package com.jacoobia.immersivezoom.client.utils;

import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

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

}
