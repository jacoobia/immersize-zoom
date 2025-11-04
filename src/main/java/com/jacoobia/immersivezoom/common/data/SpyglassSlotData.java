package com.jacoobia.immersivezoom.common.data;

import com.jacoobia.immersivezoom.common.utils.BeltPlacement;
import com.jacoobia.immersivezoom.common.utils.SpyglassType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * A minimal data class that tracks the current spyglass displayed on the players hip
 * and where on the player's belt it sits
 */
public record SpyglassSlotData(SpyglassType spyglassType, BeltPlacement placement, boolean scoping) {

    public static final SpyglassSlotData EMPTY = new SpyglassSlotData(SpyglassType.NONE, BeltPlacement.BACK_RIGHT, false);

    public static final StreamCodec<RegistryFriendlyByteBuf, SpyglassSlotData> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT.map(i -> SpyglassType.values()[i], SpyglassType::ordinal), SpyglassSlotData::spyglassType,
                    ByteBufCodecs.VAR_INT.map(i -> BeltPlacement.values()[i], BeltPlacement::ordinal), SpyglassSlotData::placement,
                    ByteBufCodecs.BOOL, SpyglassSlotData::scoping,
                    SpyglassSlotData::new
            );
}
