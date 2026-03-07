package com.jacoobia.immersivezoom.mixin.common;

import com.jacoobia.immersivezoom.common.container.SpyglassSlot;
import com.jacoobia.immersivezoom.mixin.accessors.InventoryMenuAccessor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.jacoobia.immersivezoom.common.Constants.SPYGLASS_SLOT_X;
import static com.jacoobia.immersivezoom.common.Constants.SPYGLASS_SLOT_Y;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;

/**
 * Uses {@link InventoryMenuAccessor} to add the spyglass slot to the player's inventory
 * Enables the UI interactions like hover, clicking with an item on the cursor etc.
 * Without this the slot is theoretically there but not usable on the client side
 */
@Mixin(InventoryMenu.class)
public class InventoryMenuMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addCustomSlot(Inventory playerInventory, boolean active, Player owner, CallbackInfo callback) {
        SpyglassSlot slot = new SpyglassSlot(owner, SPYGLASS_SLOT_X, SPYGLASS_SLOT_Y);
        ((InventoryMenuAccessor)this).invokeAddSlot(slot);
    }

    @Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
    private void immersiveZoom$quickMoveIntoSpyglassSlot(Player player, int index, CallbackInfoReturnable<ItemStack> callback) {
        if (index < InventoryMenu.INV_SLOT_START || index > InventoryMenu.SHIELD_SLOT) return;

        InventoryMenu menu = (InventoryMenu) (Object) this;
        Slot from = menu.slots.get(index);
        ItemStack stack = from.getItem();
        if (stack.isEmpty()) return;

        for (Slot target : menu.slots) {
            if (!(target instanceof SpyglassSlot) || target.hasItem() || !target.mayPlace(stack)) continue;

            ItemStack original = stack.copy();
            target.setByPlayer(stack.split(INTEGER_ONE));
            if (stack.isEmpty()) from.setByPlayer(ItemStack.EMPTY);
            else from.setChanged();
            callback.setReturnValue(original);
            return;
        }
    }

}
