package com.jacoobia.immersivezoom.common.network.packet;

import com.jacoobia.immersivezoom.common.data.SpyglassSlotData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

public record SpyglassSlotSyncPacket(int entityId, SpyglassSlotData data) implements CustomPacketPayload {

    public static final Type<SpyglassSlotSyncPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "hip_spyglass"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpyglassSlotSyncPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, SpyglassSlotSyncPacket::entityId,
                    SpyglassSlotData.STREAM_CODEC, SpyglassSlotSyncPacket::data,
                    SpyglassSlotSyncPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
