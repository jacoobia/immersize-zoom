package com.jacoobia.immersivezoom.mixin.common;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import com.google.common.collect.Lists;

import static com.jacoobia.immersivezoom.common.Constants.SPYGLASS_SLOT_INDEX;
import static net.minecraft.world.item.ItemStack.EMPTY;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

@Mixin(Inventory.class)
public class InventoryMixin {

    @Shadow
    @Final
    public Player player;

    @Shadow
    @Final
    @Mutable
    private List<NonNullList<ItemStack>> compartments;

    @Unique
    private final NonNullList<ItemStack> immersiveZoom$spyglassSlot = NonNullList.withSize(INTEGER_ONE, EMPTY);

    /**
     * Adds the spyglass slot to the player's inventory
     * @param player the player
     * @param callback the mixin callback
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void addExtraSlot(Player player, CallbackInfo callback) {
        this.compartments = Lists.newArrayList(this.compartments);
        this.compartments.add(immersiveZoom$spyglassSlot);
    }

    /**
     * Override the container size to include the spyglass slot
     * @param callback the mixin callback
     */
    @Inject(method = "getContainerSize", at = @At("RETURN"), cancellable = true)
    private void modifyContainerSize(CallbackInfoReturnable<Integer> callback) {
        callback.setReturnValue(callback.getReturnValue() + INTEGER_ONE);
    }

    @Inject(method = "save", at = @At("RETURN"))
    private void saveExtraSlot(ListTag listTag, CallbackInfoReturnable<ListTag> callback) {
        ItemStack spyglassStack = immersiveZoom$spyglassSlot.getFirst();
        if (!spyglassStack.isEmpty()) {
            CompoundTag compoundtag = new CompoundTag();
            compoundtag.putByte("Slot", (byte) SPYGLASS_SLOT_INDEX);
            listTag.add(spyglassStack.save(player.registryAccess(), compoundtag));
        }
    }

    @Inject(method = "load", at = @At("RETURN"))
    private void loadExtraSlot(ListTag listTag, CallbackInfo callback) {
        immersiveZoom$spyglassSlot.set(INTEGER_ZERO, EMPTY);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag compoundTag = listTag.getCompound(i);
            if((compoundTag.getByte("Slot") & 255) == SPYGLASS_SLOT_INDEX) {
                ItemStack itemstack = ItemStack.parse(player.registryAccess(), compoundTag).orElse(EMPTY);
                immersiveZoom$spyglassSlot.set(INTEGER_ZERO, itemstack);
            }
        }
    }

}
