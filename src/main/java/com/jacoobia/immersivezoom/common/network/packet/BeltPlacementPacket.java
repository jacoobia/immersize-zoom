package com.jacoobia.immersivezoom.common.network.packet;

import com.jacoobia.immersivezoom.common.utils.BeltPlacement;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

/**
 * Sent by the client to tell the server where it wants its hip spyglass
 * @param placement the client's configured belt placement
 */
public record BeltPlacementPacket(BeltPlacement placement) implements CustomPacketPayload {

    public static final Type<BeltPlacementPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "belt_placement"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BeltPlacementPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT.map(i -> BeltPlacement.values()[i], BeltPlacement::ordinal), BeltPlacementPacket::placement,
                    BeltPlacementPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
