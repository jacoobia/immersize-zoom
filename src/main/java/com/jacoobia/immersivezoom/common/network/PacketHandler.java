package com.jacoobia.immersivezoom.common.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * A generic/common packet handler for both the server and the client
 * @param <T> the packet type
 */
public interface PacketHandler<T extends CustomPacketPayload> {

    /**
     * Handles the packet
     * @param packet the packet to handle
     */
    void handle(T packet, IPayloadContext context);

}
