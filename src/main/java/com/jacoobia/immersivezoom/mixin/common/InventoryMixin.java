package com.jacoobia.immersivezoom.mixin.common;

import com.jacoobia.immersivezoom.common.Constants;
import com.jacoobia.immersivezoom.common.utils.CollectionUtils;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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

    /**
     * Gets the contents of the spyglass slot
     * @param index the slot index
     * @param callback the mixin callback
     */
    @Inject(method = "getItem", at = @At("HEAD"), cancellable = true)
    private void getItemExtraSlot(int index, CallbackInfoReturnable<ItemStack> callback) {
        if (index == SPYGLASS_SLOT_INDEX) {
            callback.setReturnValue(immersiveZoom$spyglassSlot.getFirst());
        }
    }

    /**
     * Allows the engine to know if the slot has had an item put into it
     * and updates the contents and UI
     * @param index the slot index
     * @param stack the item stack
     * @param callback the mixin callback
     */
    @Inject(method = "setItem", at = @At("HEAD"), cancellable = true)
    private void setItemExtraSlot(int index, ItemStack stack, CallbackInfo callback) {
        if (index == SPYGLASS_SLOT_INDEX && CollectionUtils.contains(Constants.ZOOM_ITEMS, stack.getItem())) {
            immersiveZoom$spyglassSlot.set(INTEGER_ZERO, stack);
            callback.cancel();
        }
    }

    /**
     * When an item is removed from the slot on the client side, update the slot
     * I think this also runs whenever code tries to remove an item from the slot
     * @param index the slot index
     * @param callback the mixin callback
     */
    @Inject(method = "removeItemNoUpdate", at = @At("HEAD"), cancellable = true)
    private void removeItemNoUpdateExtraSlot(int index, CallbackInfoReturnable<ItemStack> callback) {
        if (index == SPYGLASS_SLOT_INDEX) {
            callback.setReturnValue(immersiveZoom$spyglassSlot.set(INTEGER_ZERO, EMPTY));
        }
    }

    /**
     * When an item is removed from the slot on the server side, update the slot i.e:
     * Shift right-clicked
     * Left-clicked
     * etc.
     * @param index the slot index
     * @param count the count of items
     * @param callback the mixin callback
     */
    @Inject(method = "removeItem(II)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private void removeItemExtraSlot(int index, int count, CallbackInfoReturnable<ItemStack> callback) {
        if (index == SPYGLASS_SLOT_INDEX) {
            ItemStack item = immersiveZoom$spyglassSlot.getFirst();
            if (!item.isEmpty()) {
                immersiveZoom$spyglassSlot.set(INTEGER_ZERO, ItemStack.EMPTY);
                callback.setReturnValue(item);
            } else {
                callback.setReturnValue(ItemStack.EMPTY);
            }
        }
    }

    /**
     * When an item is removed from the slot by the player, update the slot
     * @param stack the item stack
     * @param callback the mixin callback
     */
    @Inject(method = "removeItem(Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"))
    private void removeItemByStack(ItemStack stack, CallbackInfo callback) {
        if (!stack.isEmpty() && stack == immersiveZoom$spyglassSlot.getFirst()) {
            immersiveZoom$spyglassSlot.set(INTEGER_ZERO, ItemStack.EMPTY);
        }
    }

    /**
     * Clear the spyglass slot when the inventory is cleared
     * @param callback the mixin callback
     */
    @Inject(method = "clearContent", at = @At("TAIL"))
    private void clearContentExtraSlot(CallbackInfo callback) {
        immersiveZoom$spyglassSlot.set(INTEGER_ZERO, EMPTY);
    }

    @Inject(method = "save", at = @At("RETURN"))
    private void saveExtraSlot(ListTag tag, CallbackInfoReturnable<ListTag> callback) {
        ItemStack spyglassStack = immersiveZoom$spyglassSlot.getFirst();
        if (!spyglassStack.isEmpty()) {
            CompoundTag compoundtag = new CompoundTag();
            compoundtag.putByte("spyglass_slot", (byte) SPYGLASS_SLOT_INDEX);
            tag.add(immersiveZoom$spyglassSlot.get(INTEGER_ZERO).save(player.registryAccess(), compoundtag));
        }
//        callback.setReturnValue(tag);
    }

    @Inject(method = "load", at = @At("RETURN"))
    private void loadExtraSlot(ListTag listTag, CallbackInfo callback) {
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag compoundTag = listTag.getCompound(i);
            if(compoundTag.contains("spyglass_slot")) {
                ItemStack itemstack = ItemStack.parse(player.registryAccess(), compoundTag).orElse(ItemStack.EMPTY);
                immersiveZoom$spyglassSlot.set(INTEGER_ZERO, itemstack);
            }
        }
    }


}
