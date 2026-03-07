package com.jacoobia.immersivezoom.common.item;

import com.jacoobia.immersivezoom.common.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;

public class UnattunedAmethystShard extends Item {

    public UnattunedAmethystShard() {
        super(new Item.Properties().stacksTo(INTEGER_ONE));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
                                @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Translations.Lore.unattunedTooltip.copy().withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }

}
