package com.jacoobia.immersivezoom.server.network.handlers;

import com.jacoobia.immersivezoom.common.network.PacketHandler;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassScopePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerSpyglassScopePacketHandler implements PacketHandler<SpyglassScopePacket> {

    private static final ServerSpyglassScopePacketHandler INSTANCE = new ServerSpyglassScopePacketHandler();

    public static ServerSpyglassScopePacketHandler get() {
        return INSTANCE;
    }

    private static final Map<UUID, Boolean> SCOPING = new HashMap<>();

    @Override
    public void handle(SpyglassScopePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;
            boolean was = SCOPING.getOrDefault(player.getUUID(), false);

            switch (packet.opcode()) {
                case START -> {
                    if (!was) {
                        SCOPING.put(player.getUUID(), true);
                        player.level().playSound(
                                null, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.SPYGLASS_USE, SoundSource.PLAYERS, 1.0F, 1.0F
                        );
                    }
                }

                case STOP -> {
                    if (was) {
                        SCOPING.put(player.getUUID(), false);
                        player.level().playSound(
                                null, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.SPYGLASS_STOP_USING, SoundSource.PLAYERS, 1.0F, 1.0F
                        );
                    }
                }

                default -> {}

            }
        });
    }
}
