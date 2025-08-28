package com.jacoobia.immersivezoom.common.utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

import static com.jacoobia.immersivezoom.common.Constants.SPYGLASS_SLOT_INDEX;
import static com.jacoobia.immersivezoom.common.registry.DataComponentRegistry.STACK_ID;

public class ItemUtils {

    /**
     * Finds a spyglass item in the player's hands, special slot, or inventory by its UUID.
     * @param serverPlayer The player to search within.
     * @param targetStackId The UUID of the target spyglass item.
     * @return The ItemStack of the found spyglass, or ItemStack.EMPTY if not found.
     */
    public static ItemStack findSpyglassByUuid(ServerPlayer serverPlayer, UUID targetStackId) {
        for (InteractionHand interactionHand : InteractionHand.values()) {
            ItemStack handStack = serverPlayer.getItemInHand(interactionHand);
            if (!handStack.isEmpty()
                    && targetStackId.equals(handStack.get(STACK_ID))) {
                return handStack;
            }
        }

        ItemStack specialSlotStack = serverPlayer.getInventory().getItem(SPYGLASS_SLOT_INDEX);
        if (!specialSlotStack.isEmpty()
                && targetStackId.equals(specialSlotStack.get(STACK_ID))) {
            return specialSlotStack;
        }

        Inventory inventory = serverPlayer.getInventory();
        for (int slotIndex = 0; slotIndex < inventory.getContainerSize(); slotIndex++) {
            ItemStack stackInSlot = inventory.getItem(slotIndex);
            if (!stackInSlot.isEmpty()
                    && targetStackId.equals(stackInSlot.get(STACK_ID))) {
                return stackInSlot;
            }
        }

        return ItemStack.EMPTY;
    }

    /**
     * Resolves the currently active spyglass item from the player's use state or special slot.
     * @param serverPlayer The player to check.
     * @return The ItemStack of the active spyglass, or ItemStack.EMPTY if none is active.
     */
    public static ItemStack resolveSpyglassFromSource(ServerPlayer serverPlayer) {
        if (serverPlayer.isUsingItem()) {
            ItemStack inUseStack = serverPlayer.getUseItem();
            return SpyglassUtils.isSpyglass(inUseStack) ? inUseStack : ItemStack.EMPTY;
        }

        ItemStack specialSlotStack = serverPlayer.getInventory().getItem(SPYGLASS_SLOT_INDEX);
        return SpyglassUtils.isSpyglass(specialSlotStack) ? specialSlotStack : ItemStack.EMPTY;
    }

    /**
     * Ensures the given stack has a unique UUID assigned to it.
     * @param stack The ItemStack to check and assign an ID if necessary.
     */
    public static void setItemId(ItemStack stack) {
        if (stack.get(STACK_ID) == null) {
            System.out.println("Assigning new UUID to Attuned Spyglass");
            stack.set(STACK_ID.get(), UUID.randomUUID());
        }
    }

    /**
     * Retrieves the UUID assigned to the given ItemStack.
     * @param stack The ItemStack to retrieve the ID from.
     * @return The UUID of the ItemStack, or null if none is assigned.
     */
    public static UUID getStackId(ItemStack stack) {
        return stack.get(STACK_ID);
    }

}
