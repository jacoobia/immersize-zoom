package com.jacoobia.immersivezoom.client.utils;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;

public class ClientUtils {

    private static Player localPlayer = null;

    /**
     * Get the local player from the Minecraft instance and caches it locally
     * to avoid repeated calls to Minecraft.getInstance().player
     * @return The local player
     */
    public static Player getLocalPlayer() {
        if(localPlayer == null) {
            localPlayer = Minecraft.getInstance().player;
        }
        assert Minecraft.getInstance().screen != null;
        assert localPlayer != null;
        return localPlayer;
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
     * Check if the player is in third person front view
     * @return true if the player is in third person front view
     */
    public static boolean isThirdPersonFront() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.options.getCameraType() == CameraType.THIRD_PERSON_FRONT;
    }


}
