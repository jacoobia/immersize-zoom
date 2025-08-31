package com.jacoobia.immersivezoom.client.utils;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

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
     * Returns an approximate world position of the player's main hand.
     * @param player The player entity.
     * @return A Vec3 in world space.
     */
    public static Vec3 getEstimatedHandPosition(Player player) {
        Vec3 eyePos = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        Vec3 side = look.cross(new Vec3(0, 1, 0)).normalize().scale(0.35);
        return eyePos.add(look.scale(0.6)).add(side).add(0, -0.4, 0);
    }

}
