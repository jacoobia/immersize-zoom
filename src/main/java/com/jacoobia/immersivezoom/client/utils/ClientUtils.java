package com.jacoobia.immersivezoom.client.utils;

import com.jacoobia.immersivezoom.common.data.SpyglassSlotData;
import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import com.jacoobia.immersivezoom.common.utils.SpyglassType;
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
     * Check if the player is in third person front view
     * @return true if the player is in third person front view
     */
    public static boolean isThirdPerson() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.options.getCameraType() != CameraType.FIRST_PERSON;
    }

    /**
     * Whether a player is zooming with the hotkey: the local player's own state, or the
     * server-synced flag for other players
     * @param player the player to check
     * @return true if that player is zooming with the spyglass slot hotkey
     */
    public static boolean isHotkeyScoping(Player player) {
        if (player.isLocalPlayer()) return LocalZoomState.isHotkeyScoping();
        SpyglassSlotData data = player.getData(AttachmentRegistry.HIP_SPYGLASS);
        return data.spyglassType() != SpyglassType.NONE && data.scoping();
    }


}
