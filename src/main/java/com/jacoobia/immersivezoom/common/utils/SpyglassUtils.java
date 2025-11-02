package com.jacoobia.immersivezoom.common.utils;

import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

/**
 * Utility class for handling operations related to spyglasses, including
 * determining if a player is zooming, locating spyglasses in inventory, and
 * computing magnification levels
 */
public class SpyglassUtils {

    /**
     * Checks if the given ItemStack is a spyglass
     * @param stack the ItemStack to check
     * @return true if the ItemStack is a spyglass, false otherwise
     */
    public static boolean isSpyglass(ItemStack stack) {
        return stack.is(Items.SPYGLASS) || stack.is(ItemRegistry.ATTUNED_SPYGLASS);
    }

    /**
     * Returns an approximate world position of the player's main hand
     * @param player The player entity
     * @return A Vec3 in world space
     */
    public static Vec3 getEstimatedHandPosition(Player player) {
        Vec3 eyePos = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        Vec3 side = look.cross(new Vec3(0, 1, 0)).normalize().scale(0.35);
        return eyePos.add(look.scale(0.6)).add(side).add(0, -0.4, 0);
    }

}
