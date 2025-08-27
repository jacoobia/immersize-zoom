package com.jacoobia.immersivezoom.common.network.handlers;

import com.jacoobia.immersivezoom.common.item.AttunedSpyglass;
import com.jacoobia.immersivezoom.common.network.PacketHandler;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassScopePacket;
import com.jacoobia.immersivezoom.common.utils.SpyglassUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.jacoobia.immersivezoom.common.Constants.ATTUNED_ZOOM_MAX;
import static com.jacoobia.immersivezoom.common.Constants.ATTUNED_ZOOM_MIN;
import static com.jacoobia.immersivezoom.server.utils.ServerUtils.getServerState;

public class ServerSpyglassPacketHandler implements PacketHandler<SpyglassScopePacket> {

    private static final ServerSpyglassPacketHandler INSTANCE = new ServerSpyglassPacketHandler();

    public static ServerSpyglassPacketHandler get() {
        return INSTANCE;
    }

    @Override
    public void handle(SpyglassScopePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer serverPlayer)) return;

            switch (packet.opcode()) {
                case START -> {
                    ItemStack activeSpyglassStack = SpyglassUtils.resolveSpyglassFromSpecialSlot(serverPlayer);

                    if (activeSpyglassStack.isEmpty()) return;

                    getServerState(serverPlayer).setScoping(true, activeSpyglassStack, packet.fromSlot());
                    serverPlayer.level().playSound(
                            null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(),
                            SoundEvents.SPYGLASS_USE, SoundSource.PLAYERS, 1.0F, 1.0F
                    );
                    context.reply(SpyglassScopePacket.overlay(true));
                }

                case SET_ZOOM -> {
                    ItemStack target = SpyglassUtils.resolveActiveSpyglass(serverPlayer);
                    if (target.isEmpty() || !(target.getItem() instanceof AttunedSpyglass)) return;

                    float clamped = Mth.clamp(packet.value(), ATTUNED_ZOOM_MIN, ATTUNED_ZOOM_MAX);
                    AttunedSpyglass.setZoom(target, clamped);
                    serverPlayer.inventoryMenu.broadcastChanges();
                }

                case STOP -> {
                    getServerState(serverPlayer).setScoping(false, ItemStack.EMPTY, false);
                    serverPlayer.level().playSound(
                            null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(),
                            SoundEvents.SPYGLASS_STOP_USING, SoundSource.PLAYERS, 1.0F, 1.0F
                    );
                    context.reply(SpyglassScopePacket.overlay(false));
                }

                case OVERLAY -> {
                    // ignore C2S
                }
            }
        });
    }


}
