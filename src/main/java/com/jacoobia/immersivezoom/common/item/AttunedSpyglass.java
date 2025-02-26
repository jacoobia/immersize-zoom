package com.jacoobia.immersivezoom.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.jacoobia.immersivezoom.common.registry.DataComponentRegistry.*;
import static org.apache.commons.lang3.math.NumberUtils.FLOAT_ONE;

public class AttunedSpyglass extends SpyglassItem {

    public static final int USE_DURATION = 1400;

    private static final String ZOOM_COMPONENT = "tooltip.immersive_zoom.zoom_level";

    public AttunedSpyglass(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return USE_DURATION;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
                                @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {

        if(stack.get(ZOOM_FACTOR) != null) {
            tooltipComponents.add(Component.translatable(ZOOM_COMPONENT, stack.get(ZOOM_FACTOR)));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    public static void setZoomLevel(ItemStack stack, float zoom) {
        if (stack.isEmpty() || !(stack.getItem() instanceof AttunedSpyglass)) return;
        stack.set(ZOOM_FACTOR, zoom);
    }

    public static float getZoomLevel(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof AttunedSpyglass)) return FLOAT_ONE;
        Float zoomFactor = stack.get(ZOOM_FACTOR);
        return zoomFactor != null ? zoomFactor : FLOAT_ONE;
    }

}
