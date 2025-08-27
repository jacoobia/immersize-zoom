package com.jacoobia.immersivezoom.server;

import net.minecraft.world.item.ItemStack;

public interface SpyglassState {

    /**
     * Sets whether the player is scoping with a spyglass
     * @param scoping whether the player is scoping
     * @param source the ItemStack being used to scope
     * @param fromSlot whether the ItemStack is from the special slot
     */
    void setScoping(boolean scoping, ItemStack source, boolean fromSlot);

    /**
     * Checks if the player is currently scoping with a spyglass
     * @return true if the player is scoping, false otherwise
     */
    boolean isScoping();

    /**
     * Checks if the ItemStack being used to scope is from the special slot
     * @return true if the ItemStack is from the special slot, false otherwise
     */
    boolean fromSlot();

    /**
     * Gets the ItemStack being used to scope
     * @return the ItemStack being used to scope
     */
    ItemStack sourceStack();

}
