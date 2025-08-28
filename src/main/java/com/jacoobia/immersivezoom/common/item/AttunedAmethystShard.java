package com.jacoobia.immersivezoom.common.item;

import com.jacoobia.immersivezoom.common.registry.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;

/**
 * Item representing an attuned amethyst shard, used for crafting attuned spyglasses
 */
public class AttunedAmethystShard extends Item {

    public AttunedAmethystShard() {
        super(new Item.Properties().stacksTo(INTEGER_ONE));
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return super.getName(stack)
                .copy()
                .withStyle(style -> style.withColor(ChatFormatting.GOLD));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        int total = stack.get(DataComponentRegistry.TESTER) != null ? stack.get(DataComponentRegistry.TESTER) : 0;
        tooltipComponents.add(Component.literal(String.valueOf(total)).withStyle(ChatFormatting.GRAY));
    }
}
