package com.jacoobia.immersivezoom.server.network.handlers;

import com.jacoobia.immersivezoom.common.item.AttunedSpyglass;
import com.jacoobia.immersivezoom.common.network.PacketHandler;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassZoomPacket;
import com.jacoobia.immersivezoom.common.utils.ItemUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

import static com.jacoobia.immersivezoom.common.registry.DataComponentRegistry.ZOOM_FACTOR;
import static com.jacoobia.immersivezoom.common.utils.ItemUtils.findSpyglassByUuid;
import static com.jacoobia.immersivezoom.common.utils.ItemUtils.resolveSpyglassFromSource;

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
            UUID targetStackId = packet.stackId();

            ItemStack targetStack = ItemStack.EMPTY;

            if (targetStackId != null) {
                targetStack = findSpyglassByUuid(player, targetStackId);
            }

            if (targetStack.isEmpty()) {
                targetStack = resolveSpyglassFromSource(player);
                if (!targetStack.isEmpty()) {
                    ItemUtils.setItemId(targetStack);
                }
            }

            if (!targetStack.isEmpty() && targetStack.getItem() instanceof AttunedSpyglass) {
                targetStack.set(ZOOM_FACTOR, requestedZoom);
                player.getInventory().setChanged();
                player.inventoryMenu.broadcastChanges();
            }
        });
    }

}
