package com.jacoobia.immersivezoom.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import static com.jacoobia.immersivezoom.common.Constants.*;
import static com.jacoobia.immersivezoom.common.registry.DataComponentRegistry.ZOOM_FACTOR;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;

public class AttunedSpyglass extends SpyglassItem {

    public static final int USE_DURATION = 1400;

    public AttunedSpyglass() {
        super(new Item.Properties().stacksTo(INTEGER_ONE)
                .component(ZOOM_FACTOR.get(), ATTUNED_ZOOM_DEFAULT));
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return USE_DURATION;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
                                @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {

        Float storedZoom = stack.get(ZOOM_FACTOR.get());
        float zoom = storedZoom != null ? Mth.clamp(storedZoom, ATTUNED_ZOOM_MIN, ATTUNED_ZOOM_MAX)
                : ATTUNED_ZOOM_DEFAULT;

        tooltipComponents.add(Component.translatable(
                "tooltip.immersive_zoom.zoom_level",
                String.format(Locale.ROOT, "%.2fx", zoom)
        ));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return super.getName(stack)
                .copy()
                .withStyle(style -> style.withColor(ChatFormatting.GOLD));
    }

    /**
     * Clamp the zoom value to be within the allowed range
     * @param stack the item stack
     * @return the clamped zoom value
     */
    public static float getZoom(ItemStack stack) {
        Float zoomFactor = stack.get(ZOOM_FACTOR.get());
        return zoomFactor != null ? Mth.clamp(zoomFactor, ATTUNED_ZOOM_MIN, ATTUNED_ZOOM_MAX) : ATTUNED_ZOOM_DEFAULT;
    }

    /**
     * Set the zoom value, clamped to the allowed range
     * @param stack the item stack
     * @param zoomFactor the zoom factor to set
     */
    public static void setZoom(ItemStack stack, float zoomFactor) {
        if (stack.isEmpty() || !(stack.getItem() instanceof AttunedSpyglass)) return;
        float clampedZoom = Mth.clamp(zoomFactor, ATTUNED_ZOOM_MIN, ATTUNED_ZOOM_MAX);
        stack.set(ZOOM_FACTOR.get(), clampedZoom);
    }

}
