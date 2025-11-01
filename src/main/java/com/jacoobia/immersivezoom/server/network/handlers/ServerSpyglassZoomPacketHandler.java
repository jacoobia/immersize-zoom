package com.jacoobia.immersivezoom.server.network.handlers;

import com.jacoobia.immersivezoom.common.item.AttunedSpyglass;
import com.jacoobia.immersivezoom.common.network.PacketHandler;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassZoomPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.jacoobia.immersivezoom.common.Constants.SPYGLASS_SLOT_INDEX;
import static com.jacoobia.immersivezoom.common.registry.DataComponentRegistry.ZOOM_FACTOR;

public class ServerSpyglassZoomPacketHandler implements PacketHandler<SpyglassZoomPacket> {

    private static final ServerSpyglassZoomPacketHandler INSTANCE = new ServerSpyglassZoomPacketHandler();

    public static ServerSpyglassZoomPacketHandler get() {
        return INSTANCE;
    }

    @Override
    public void handle(SpyglassZoomPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;
            float requestedZoom = packet.value();

            ItemStack targetStack = packet.fromSlot()
                    ? player.getInventory().getItem(SPYGLASS_SLOT_INDEX)
                    : player.getUseItem();

            if (!targetStack.isEmpty() && targetStack.getItem() instanceof AttunedSpyglass) {
                targetStack.set(ZOOM_FACTOR, requestedZoom);
                player.getInventory().setChanged();
                player.inventoryMenu.broadcastChanges();
            }
        });
    }

}
