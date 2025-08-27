package com.jacoobia.immersivezoom.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

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

}
