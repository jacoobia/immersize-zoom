package com.jacoobia.immersivezoom.common.network.Codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Deprecated in favour of just sending a bool since the game already knows
 * which item is being used if it isn't being used from our custom slot
 */
@Deprecated
public class CustomCodecs {

    /**
     * Stream codec for a {@link UUID}
     */
    public static final StreamCodec<ByteBuf, UUID> UUID_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull UUID decode(@NotNull ByteBuf buf) {
            return UUIDUtil.STREAM_CODEC.decode(buf);
        }

        @Override
        public void encode(@NotNull ByteBuf buf, @NotNull UUID uuid) {
            UUIDUtil.STREAM_CODEC.encode(buf, uuid);
        }
    };

    /**
     * NBT Codec for a {@link UUID}
     */
    public static final Codec<UUID> UUID_NBT_CODEC =
            Codec.STRING.comapFlatMap(
                    s -> {
                        try {
                            return DataResult.success(UUID.fromString(s));
                        } catch (IllegalArgumentException e) {
                            return DataResult.error(() -> "Invalid UUID: " + s);
                        }
                    },
                    UUID::toString
            );


}
