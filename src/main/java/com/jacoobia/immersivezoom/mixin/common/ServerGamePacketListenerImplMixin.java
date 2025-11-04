package com.jacoobia.immersivezoom.mixin.common;

import com.jacoobia.immersivezoom.common.container.SpyglassSlot;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Creative mode slot support for the spyglass slot since it has an index
 * that the base game doesn't look for
 */
@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @Inject(method = "handleSetCreativeModeSlot",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/server/level/ServerLevel;)V",
                    shift = At.Shift.AFTER),
            cancellable = true)
    private void immersiveZoom$acceptSpyglassSlot(ServerboundSetCreativeModeSlotPacket packet, CallbackInfo callback) {
        ServerPlayer player = ((ServerGamePacketListenerImpl) (Object) this).player;
        if (!player.gameMode.isCreative()) return;

        int slotNum = packet.slotNum();
        if (slotNum < 0 || slotNum >= player.inventoryMenu.slots.size()) return;
        if (!(player.inventoryMenu.getSlot(slotNum) instanceof SpyglassSlot slot)) return;

        ItemStack stack = packet.itemStack();
        if (!stack.isEmpty() && (!slot.mayPlace(stack) || stack.getCount() > stack.getMaxStackSize())) return;

        slot.setByPlayer(stack);
        player.inventoryMenu.broadcastChanges();
        callback.cancel();
    }

}
