package com.jacoobia.immersivezoom.server.network.handlers;

import com.jacoobia.immersivezoom.common.container.SpyglassSlot;
import com.jacoobia.immersivezoom.common.data.SpyglassSlotData;
import com.jacoobia.immersivezoom.common.network.PacketHandler;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassScopePacket;
import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import com.jacoobia.immersivezoom.common.utils.SpyglassUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.jacoobia.immersivezoom.common.Constants.SPYGLASS_SLOT_INDEX;

public class ServerSpyglassScopePacketHandler implements PacketHandler<SpyglassScopePacket> {

    private static final ServerSpyglassScopePacketHandler INSTANCE = new ServerSpyglassScopePacketHandler();

    public static ServerSpyglassScopePacketHandler get() {
        return INSTANCE;
    }

    @Override
    public void handle(SpyglassScopePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;
            boolean was = player.getData(AttachmentRegistry.HIP_SPYGLASS).scoping();

            switch (packet.opcode()) {
                case START -> {
                    // Only hotkey zoom sends START, so the spyglass must be in the slot
                    if (!was && SpyglassUtils.isSpyglass(player.getInventory().getItem(SPYGLASS_SLOT_INDEX))) {
                        setScoping(player, true);
                        player.level().playSound(
                                null, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.SPYGLASS_USE, SoundSource.PLAYERS, 1.0F, 1.0F
                        );
                    }
                }

                case STOP -> {
                    if (was) {
                        setScoping(player, false);
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

    private static void setScoping(ServerPlayer player, boolean scoping) {
        var data = player.getData(AttachmentRegistry.HIP_SPYGLASS);
        player.setData(AttachmentRegistry.HIP_SPYGLASS, new SpyglassSlotData(data.spyglassType(), data.placement(), data.visible(), scoping));
        SpyglassSlot.syncSlotContent(player);
    }

}
