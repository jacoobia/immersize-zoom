package com.jacoobia.immersivezoom.common.network.packet;

import com.jacoobia.immersivezoom.common.network.SplyglassOpcode;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static com.jacoobia.immersivezoom.common.Constants.*;

public record SpyglassScopePacket(SplyglassOpcode opcode,
                                  boolean fromSlot,
                                  float value,
                                  boolean scoping) implements CustomPacketPayload {

    public static final Type<SpyglassScopePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "spyglass_zoom"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpyglassScopePacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, (SpyglassScopePacket packet) -> packet.opcode().ordinal(),
                    ByteBufCodecs.BOOL,    SpyglassScopePacket::fromSlot,
                    ByteBufCodecs.FLOAT,   SpyglassScopePacket::value,
                    ByteBufCodecs.BOOL,    SpyglassScopePacket::scoping,
                    (opcode, fromSlot, value, scoping) ->
                            new SpyglassScopePacket(SplyglassOpcode.values()[opcode], fromSlot, value, scoping)
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static SpyglassScopePacket start(boolean fromSlot) { return new SpyglassScopePacket(SplyglassOpcode.START, fromSlot, 0f, false); }
    public static SpyglassScopePacket stop()                 { return new SpyglassScopePacket(SplyglassOpcode.STOP, false, 0f, false); }
    public static SpyglassScopePacket setFinal(float zoom)   { return new SpyglassScopePacket(SplyglassOpcode.SET_ZOOM, false, zoom, false); }
    public static SpyglassScopePacket overlay(boolean scoping)     { return new SpyglassScopePacket(SplyglassOpcode.OVERLAY, false, 0f, scoping); }

}
