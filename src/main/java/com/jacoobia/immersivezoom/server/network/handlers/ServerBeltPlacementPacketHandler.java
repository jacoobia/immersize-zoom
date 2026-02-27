package com.jacoobia.immersivezoom.server.network.handlers;

import com.jacoobia.immersivezoom.common.container.SpyglassSlot;
import com.jacoobia.immersivezoom.common.data.SpyglassSlotData;
import com.jacoobia.immersivezoom.common.network.PacketHandler;
import com.jacoobia.immersivezoom.common.network.packet.BeltPlacementPacket;
import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerBeltPlacementPacketHandler implements PacketHandler<BeltPlacementPacket> {

    private static final ServerBeltPlacementPacketHandler INSTANCE = new ServerBeltPlacementPacketHandler();

    public static ServerBeltPlacementPacketHandler get() {
        return INSTANCE;
    }

    @Override
    public void handle(BeltPlacementPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;

            var data = player.getData(AttachmentRegistry.HIP_SPYGLASS);
            if (data.placement() == packet.placement() && data.visible() == packet.visible()) return;

            player.setData(AttachmentRegistry.HIP_SPYGLASS,
                    new SpyglassSlotData(data.stack(), packet.placement(), packet.visible(), data.scoping()));
            SpyglassSlot.syncSlotContent(player);
        });
    }

}
