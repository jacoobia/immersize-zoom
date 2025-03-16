package com.jacoobia.immersivezoom.common.utils;

import com.jacoobia.immersivezoom.common.Constants;
import net.minecraft.world.item.ItemStack;

public class SpyglassUtils {

    public static boolean isSpyglass(ItemStack stack) {
        return CollectionUtils.contains(Constants.ZOOM_ITEMS, stack.getItem());
    }

}
