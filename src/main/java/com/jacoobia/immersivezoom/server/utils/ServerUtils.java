package com.jacoobia.immersivezoom.server.utils;

import com.jacoobia.immersivezoom.common.data.SpyglassSlotData;
import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import com.jacoobia.immersivezoom.common.utils.HipSide;
import com.jacoobia.immersivezoom.common.utils.SpyglassType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ServerUtils {

    /**
     * Sets the hip item for the given player
     * todo: replace the hip side default with a config option
     * @param player the player to set the hip item for
     * @param stack the item stack to set as the hip item
     */
    public static void setHipItem(ServerPlayer player, ItemStack stack) {
        SpyglassType type = SpyglassType.fromItemStack(stack);
        boolean scoping = player.getData(AttachmentRegistry.HIP_SPYGLASS).scoping();
        player.setData(AttachmentRegistry.HIP_SPYGLASS, new SpyglassSlotData(type, HipSide.RIGHT, scoping));
    }

}
