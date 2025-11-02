package com.jacoobia.immersivezoom.common.network.packet;

import com.jacoobia.immersivezoom.common.network.opcode.SpyglassOpcode;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

public record SpyglassScopePacket(SpyglassOpcode opcode) implements CustomPacketPayload {

    public static final Type<SpyglassScopePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "spyglass_scope"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpyglassScopePacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, (SpyglassScopePacket packet) -> packet.opcode().ordinal(),
                    (opcode) ->
                            new SpyglassScopePacket(SpyglassOpcode.values()[opcode])
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static SpyglassScopePacket start() {
        return new SpyglassScopePacket(SpyglassOpcode.START);
    }

    public static SpyglassScopePacket stop() {
        return new SpyglassScopePacket(SpyglassOpcode.STOP);
    }


}
