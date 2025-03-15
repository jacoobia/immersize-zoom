package com.jacoobia.immersivezoom.common.item;

import com.jacoobia.immersivezoom.common.Translations;
import com.jacoobia.immersivezoom.common.utils.NumberUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.jacoobia.immersivezoom.common.Constants.*;
import static com.jacoobia.immersivezoom.common.registry.DataComponentRegistry.RENDER_LENS;
import static com.jacoobia.immersivezoom.common.registry.DataComponentRegistry.ZOOM_FACTOR;
import static org.apache.commons.lang3.math.NumberUtils.FLOAT_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;

public class AttunedSpyglass extends SpyglassItem {

    public static final int USE_DURATION = 1400;

    public AttunedSpyglass() {
        super(new Item.Properties().stacksTo(INTEGER_ONE));
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return USE_DURATION;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
                                @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {

        if(stack.get(ZOOM_FACTOR) != null) {
            @SuppressWarnings("DataFlowIssue") float zoomLevel = stack.get(ZOOM_FACTOR);
            double percentage = NumberUtils.calculatePercentage(zoomLevel, MIN_ZOOM, MAX_ZOOM);
            tooltipComponents.add(Translations.Tooltips.zoomLevel(percentage));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    /**
     * Set the zoom level of the spyglass
     * @param stack The spyglass item stack
     * @param zoom The zoom level
     */
    public static void setZoomLevel(ItemStack stack, float zoom) {
        if (stack.isEmpty() || !(stack.getItem() instanceof AttunedSpyglass)) return;
        stack.set(ZOOM_FACTOR, zoom);
    }

    /**
     * Get the zoom level of the spyglass
     * @param stack The spyglass item stack
     * @return The zoom level
     */
    public static float getZoomLevel(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof AttunedSpyglass)) return FLOAT_ONE;
        Float zoomFactor = stack.get(ZOOM_FACTOR);
        return zoomFactor != null ? zoomFactor : DEFAULT_ZOOM;
    }

    /**
     * Set whether the lens should be rendered
     * @param stack The spyglass item stack
     * @param renderLens Whether the lens should be rendered
     */
    public static void setRenderLens(ItemStack stack, boolean renderLens) {
        if (stack.isEmpty() || !(stack.getItem() instanceof AttunedSpyglass)) return;
        stack.set(RENDER_LENS, renderLens);
    }

    /**
     * Get whether the lens should be rendered
     * @param stack The spyglass item stack
     * @return Whether the lens should be rendered
     */
    public static boolean shouldRenderLens(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof AttunedSpyglass)) return false;
        Boolean renderLens = stack.get(RENDER_LENS);
        return renderLens != null && renderLens;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return super.getName(stack)
                .copy()
                .withStyle(style -> style.withColor(ChatFormatting.GOLD));
    }

}
