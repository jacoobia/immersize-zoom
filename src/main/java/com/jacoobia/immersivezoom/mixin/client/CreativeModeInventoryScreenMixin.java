package com.jacoobia.immersivezoom.mixin.client;

import com.jacoobia.immersivezoom.common.container.SpyglassSlot;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static com.jacoobia.immersivezoom.common.Constants.CREATIVE_SPYGLASS_SLOT_X;
import static com.jacoobia.immersivezoom.common.Constants.CREATIVE_SPYGLASS_SLOT_Y;

/**
 * Overrides the slot position in the creative menu for the spyglass slot so that
 * it doesn't get put into the hotbar row
 */
@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {

    @Unique
    private static final String SLOT_WRAPPER_INIT =
            "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen$SlotWrapper;<init>(Lnet/minecraft/world/inventory/Slot;III)V";

    @ModifyArg(method = "selectTab", at = @At(value = "INVOKE", target = SLOT_WRAPPER_INIT), index = 2)
    private int immersiveZoom$spyglassSlotX(Slot slot, int index, int x, int y) {
        return slot instanceof SpyglassSlot ? CREATIVE_SPYGLASS_SLOT_X : x;
    }

    @ModifyArg(method = "selectTab", at = @At(value = "INVOKE", target = SLOT_WRAPPER_INIT), index = 3)
    private int immersiveZoom$spyglassSlotY(Slot slot, int index, int x, int y) {
        return slot instanceof SpyglassSlot ? CREATIVE_SPYGLASS_SLOT_Y : y;
    }

}
