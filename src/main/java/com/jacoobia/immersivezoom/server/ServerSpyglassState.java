package com.jacoobia.immersivezoom.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class ServerSpyglassState implements SpyglassState, INBTSerializable<CompoundTag> {

    private boolean scoping;
    private boolean fromSlot;
    private ItemStack source = ItemStack.EMPTY;

    @Override
    public void setScoping(boolean scoping, ItemStack source, boolean fromSlot) {
        this.scoping = scoping;
        this.fromSlot = fromSlot;
        this.source = source.copy();
    }
    @Override
    public boolean isScoping() {
        return scoping;
    }

    @Override
    public boolean fromSlot() {
        return fromSlot;
    }

    @Override
    public ItemStack sourceStack() {
        return source;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Scoping", scoping);
        tag.putBoolean("FromSlot", fromSlot);
        if (!source.isEmpty()) {
            tag.put("Source", source.save(provider));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag tag) {
        scoping = tag.getBoolean("Scoping");
        fromSlot = tag.getBoolean("FromSlot");
        if (tag.contains("Source", Tag.TAG_COMPOUND)) {
            source = ItemStack.parse(provider, tag.getCompound("Source"))
                    .orElse(ItemStack.EMPTY);
        } else {
            source = ItemStack.EMPTY;
        }
    }
}
