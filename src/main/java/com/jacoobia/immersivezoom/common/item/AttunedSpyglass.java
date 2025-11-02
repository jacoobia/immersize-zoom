package com.jacoobia.immersivezoom.common.item;

import com.jacoobia.immersivezoom.common.data.AttunedZoomBarTooltip;
import com.jacoobia.immersivezoom.common.utils.DataUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.jacoobia.immersivezoom.common.Constants.*;
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
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide) {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.getInventory().setChanged();
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack stack) {
        float zoom = DataUtils.getFloatOrDefault(stack, ZOOM_FACTOR, ATTUNED_ZOOM_DEFAULT);
        float progress = (zoom - ATTUNED_ZOOM_MIN) / (ATTUNED_ZOOM_MAX - ATTUNED_ZOOM_MIN);
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
