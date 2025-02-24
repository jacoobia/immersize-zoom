package com.jacoobia.immersivezoom.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import org.jetbrains.annotations.NotNull;

public class AttunedSpyglass extends SpyglassItem {

    public static final int USE_DURATION = 1400;

    public AttunedSpyglass(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return USE_DURATION;
    }

}
