package com.jacoobia.immersivezoom.common.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.NotNull;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

/**
 * Tells the server the new zoom level of the spyglass being looked through.
 * Names where that spyglass is (slot or hand) rather than "the item in use", because zoom
 * packets are throttled and can arrive just after the player stops using an in-hand spyglass.
 * @param source where the spyglass is
 * @param value the zoom factor of that specific spyglass
 */
public record SpyglassZoomPacket(Source source, float value) implements CustomPacketPayload {

    public enum Source { SLOT, MAIN_HAND, OFF_HAND }

    public static final Type<SpyglassZoomPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "spyglass_zoom"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpyglassZoomPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT.map(i -> Source.values()[i], Source::ordinal), SpyglassZoomPacket::source,
                    ByteBufCodecs.FLOAT, SpyglassZoomPacket::value,
                    SpyglassZoomPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * @param hand the hand holding the spyglass, or null for the spyglass slot
     * @param value the new zoom factor
     */
    public static SpyglassZoomPacket create(InteractionHand hand, float value) {
        Source source = hand == null ? Source.SLOT
                : hand == InteractionHand.MAIN_HAND ? Source.MAIN_HAND : Source.OFF_HAND;
        return new SpyglassZoomPacket(source, value);
    }

}
