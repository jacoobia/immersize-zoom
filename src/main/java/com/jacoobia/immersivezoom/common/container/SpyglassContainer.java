package com.jacoobia.immersivezoom.common.container;

import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import com.jacoobia.immersivezoom.common.utils.SpyglassUtils;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

/**
 * Rather than expanding the inventory with a mixin to add a new slot
 * moved to using a container that just renders when the player inventory is open
 * so technically it's a different container. Should avoid collision with other mods
 * that also touch the inventory (which is nice for the modpack we're playing with)
 */
@RequiredArgsConstructor
public class SpyglassContainer implements Container {

    private final Player owner;

    @Override
    public int getContainerSize() {
        return INTEGER_ONE;
    }

    @Override
    public boolean isEmpty() {
        return SpyglassUtils.getSlotItem(owner).isEmpty();
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return SpyglassUtils.getSlotItem(owner);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack removed = getItem(slot).split(amount);
        if (getItem(slot).isEmpty()) setItem(slot, ItemStack.EMPTY);
        return removed;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        ItemStack removed = getItem(slot);
        setItem(slot, ItemStack.EMPTY);
        return removed;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        owner.setData(AttachmentRegistry.SPYGLASS_SLOT, stack);
    }

    @Override
    public void setChanged() {}

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        setItem(INTEGER_ZERO, ItemStack.EMPTY);
    }

}
