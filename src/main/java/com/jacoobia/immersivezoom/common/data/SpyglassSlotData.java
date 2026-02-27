package com.jacoobia.immersivezoom.common.data;

import com.jacoobia.immersivezoom.common.utils.BeltPlacement;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import static com.jacoobia.immersivezoom.common.Constants.DEFAULT_BELT_PLACEMENT;
import static com.jacoobia.immersivezoom.common.Constants.DEFAULT_SHOW_BELT_SPYGLASS;

/**
 * A minimal data class that tracks the current spyglass displayed on the players hip
 * and where on the player's belt it sits
 */
public record SpyglassSlotData(ItemStack stack, BeltPlacement placement, boolean visible, boolean scoping) {

    public static final SpyglassSlotData EMPTY = new SpyglassSlotData(ItemStack.EMPTY, DEFAULT_BELT_PLACEMENT, DEFAULT_SHOW_BELT_SPYGLASS, false);

    public static final StreamCodec<RegistryFriendlyByteBuf, SpyglassSlotData> STREAM_CODEC =
            StreamCodec.composite(
                    ItemStack.OPTIONAL_STREAM_CODEC, SpyglassSlotData::stack,
                    ByteBufCodecs.VAR_INT.map(i -> BeltPlacement.values()[i], BeltPlacement::ordinal), SpyglassSlotData::placement,
                    ByteBufCodecs.BOOL, SpyglassSlotData::visible,
                    ByteBufCodecs.BOOL, SpyglassSlotData::scoping,
                    SpyglassSlotData::new
            );
}
