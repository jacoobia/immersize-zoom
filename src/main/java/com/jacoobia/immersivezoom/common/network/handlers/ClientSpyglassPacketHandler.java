package com.jacoobia.immersivezoom.common.network.handlers;

import com.jacoobia.immersivezoom.client.ClientSpyglassState;
import com.jacoobia.immersivezoom.common.network.PacketHandler;
import com.jacoobia.immersivezoom.common.network.SplyglassOpcode;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassScopePacket;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientSpyglassPacketHandler implements PacketHandler<SpyglassScopePacket> {

    private static final ClientSpyglassPacketHandler INSTANCE = new ClientSpyglassPacketHandler();
    public static ClientSpyglassPacketHandler get() { return INSTANCE; }

    @Override
    public void handle(SpyglassScopePacket packet, IPayloadContext context) {
        if (packet.opcode() != SplyglassOpcode.OVERLAY) return;
        context.enqueueWork(() -> ClientSpyglassState.setScoping(packet.scoping()));
    }

}
