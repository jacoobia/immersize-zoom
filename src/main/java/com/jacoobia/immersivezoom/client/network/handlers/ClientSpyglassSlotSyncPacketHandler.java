package com.jacoobia.immersivezoom.client.network.handlers;

import com.jacoobia.immersivezoom.common.network.PacketHandler;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassSlotSyncPacket;
import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientSpyglassSlotSyncPacketHandler implements PacketHandler<SpyglassSlotSyncPacket> {

    private static final ClientSpyglassSlotSyncPacketHandler INSTANCE = new ClientSpyglassSlotSyncPacketHandler();

    public static ClientSpyglassSlotSyncPacketHandler get() {
        return INSTANCE;
    }

    @Override
    public void handle(SpyglassSlotSyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var minecraft = Minecraft.getInstance();
            if (minecraft.level == null) return;
            var entity = minecraft.level.getEntity(packet.entityId());
            if (entity instanceof AbstractClientPlayer clientPlayer) {
                clientPlayer.setData(AttachmentRegistry.HIP_SPYGLASS, packet.data());
            }
        });
    }

}
