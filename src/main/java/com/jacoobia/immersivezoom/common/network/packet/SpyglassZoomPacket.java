package com.jacoobia.immersivezoom.common.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

/**
 * Lets the server/client know if the zoom is coming from the special slot
 * or from the item in the players hand, now skips all the confusing UUID tracking
 * and lookup since only spyglass can be in use at any one time
 * @param fromSlot true/false if the spyglass in use is in the custom spyglass slot
 * @param value the zoom factor of that specific spyglass
 */
public record SpyglassZoomPacket(boolean fromSlot,
                                 float value) implements CustomPacketPayload {

    public static final Type<SpyglassZoomPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "spyglass_zoom"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpyglassZoomPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, SpyglassZoomPacket::fromSlot,
                    ByteBufCodecs.FLOAT, SpyglassZoomPacket::value,
                    SpyglassZoomPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static SpyglassZoomPacket create(boolean fromSlot, float value) {
        return new SpyglassZoomPacket(fromSlot, value);
    }

}
