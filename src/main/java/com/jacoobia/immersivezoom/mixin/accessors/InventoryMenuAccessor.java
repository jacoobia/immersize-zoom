package com.jacoobia.immersivezoom.mixin.accessors;

import com.jacoobia.immersivezoom.mixin.common.InventoryMenuMixin;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Exposes {@link AbstractContainerMenu#addSlot(Slot)} to be used in
 * {@link InventoryMenuMixin}So that we can register a slot in the
 * player's inventory
 * <p>
 * Suppressing UnusedReturnValue warning because the return value
 */
@SuppressWarnings("UnusedReturnValue")
@Mixin(AbstractContainerMenu.class)
public interface InventoryMenuAccessor {

    @Invoker("addSlot")
    Slot invokeAddSlot(Slot slot);
}