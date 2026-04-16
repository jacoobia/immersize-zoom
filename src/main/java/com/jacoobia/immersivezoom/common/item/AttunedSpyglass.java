package com.jacoobia.immersivezoom.common.item;

import com.jacoobia.immersivezoom.common.data.AttunedZoomBarTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.jacoobia.immersivezoom.common.Constants.ATTUNED_ZOOM_DEFAULT;
import static com.jacoobia.immersivezoom.common.Constants.ATTUNED_ZOOM_MAX;
import static com.jacoobia.immersivezoom.common.Constants.ATTUNED_ZOOM_MIN;
import static com.jacoobia.immersivezoom.common.registry.DataComponentRegistry.ZOOM_FACTOR;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;

public class AttunedSpyglass extends SpyglassItem {

    public static final int USE_DURATION = 1200;

    public AttunedSpyglass() {
        super(new Item.Properties().stacksTo(INTEGER_ONE));
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return USE_DURATION;
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack stack) {
        float zoom = stack.getOrDefault(ZOOM_FACTOR, ATTUNED_ZOOM_DEFAULT);
        // Log scale to match the multiplicative scroll steps: every notch moves the bar the same amount
        float progress = (float) (Math.log(zoom / ATTUNED_ZOOM_MIN) / Math.log(ATTUNED_ZOOM_MAX / ATTUNED_ZOOM_MIN));
        progress = Mth.clamp(progress, 0f, 1f);
        return Optional.of(new AttunedZoomBarTooltip(progress, zoom));
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return super.getName(stack)
                .copy()
                .withStyle(style -> style.withColor(ChatFormatting.GOLD));
    }

}
