package com.jacoobia.immersivezoom.common.data;

import com.jacoobia.immersivezoom.common.utils.HipSide;
import com.jacoobia.immersivezoom.common.utils.SpyglassType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * A minimal data class that tracks the current spyglass displayed on the players hip
 * and the side of the player it is on.
 */
public record SpyglassSlotData(SpyglassType spyglassType, HipSide side, boolean scoping) {

    public static final SpyglassSlotData EMPTY = new SpyglassSlotData(SpyglassType.NONE, HipSide.RIGHT, false);

    public static final StreamCodec<RegistryFriendlyByteBuf, SpyglassSlotData> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT.map(i -> SpyglassType.values()[i], SpyglassType::ordinal), SpyglassSlotData::spyglassType,
                    ByteBufCodecs.VAR_INT.map(i -> HipSide.values()[i], HipSide::ordinal), SpyglassSlotData::side,
                    ByteBufCodecs.BOOL, SpyglassSlotData::scoping,
                    SpyglassSlotData::new
            );
}
