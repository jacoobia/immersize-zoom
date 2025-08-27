package com.jacoobia.immersivezoom.mixin.common;

import com.jacoobia.immersivezoom.client.ui.container.SpyglassSlot;
import com.jacoobia.immersivezoom.common.utils.CollectionUtils;
import com.jacoobia.immersivezoom.mixin.accessors.InventoryMenuAccessor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.jacoobia.immersivezoom.common.Constants.SPYGLASS_SLOT_INDEX;
import static com.jacoobia.immersivezoom.common.Constants.ZOOM_ITEMS;

/**
 * Uses {@link InventoryMenuAccessor} to add the spyglass slot to the player's inventory
 * Enables the UI interactions like hover, clicking with an item on the cursor etc.
 * Without this the slot is theoretically there but not usable on the client side
 */
@Mixin(InventoryMenu.class)
public class InventoryMenuMixin {

    @Unique
    private static final int X_POSITION = 77;
    @Unique
    private static final int Y_POSITION = 44;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addCustomSlot(Inventory playerInventory, boolean active, Player owner, CallbackInfo callback) {
        SpyglassSlot slot = new SpyglassSlot(playerInventory, SPYGLASS_SLOT_INDEX, X_POSITION, Y_POSITION) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return CollectionUtils.contains(ZOOM_ITEMS, stack.getItem());
            }
        };

        ((InventoryMenuAccessor)this).invokeAddSlot(slot);
    }

}
