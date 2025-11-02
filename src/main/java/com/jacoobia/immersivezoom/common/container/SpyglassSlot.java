package com.jacoobia.immersivezoom.common.container;

import com.jacoobia.immersivezoom.common.utils.SpyglassUtils;
import com.jacoobia.immersivezoom.common.data.SpyglassSlotData;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassSlotSyncPacket;
import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.jacoobia.immersivezoom.common.Constants.EMPTY_SLOT_SPYGLASS;
import static net.minecraft.world.inventory.InventoryMenu.BLOCK_ATLAS;

public class SpyglassSlot extends Slot {

    private final Player owner;

    public SpyglassSlot(Container container, int slot, int x, int y, Player owner) {
        super(container, slot, x, y);
        this.owner = owner;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return SpyglassUtils.isSpyglass(stack);
    }

    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(BLOCK_ATLAS, EMPTY_SLOT_SPYGLASS);
    }

    @Override
    public void set(@NotNull ItemStack stack) {
        ItemStack before = this.getItem().copy();
        super.set(stack);

        if (!ItemStack.isSameItemSameComponents(before, stack)) {
            onContentsChanged();
        }
    }

    @Override
    public void onTake(@NotNull Player player, @NotNull ItemStack taken) {
        super.onTake(player, taken);
        onContentsChanged();
    }

    private void onContentsChanged() {
        if (owner.level().isClientSide) return;
        if (!(owner instanceof ServerPlayer player)) return;

//        ItemStack current = this.getItem();
//        ServerUtils.setHipItem(player, current.isEmpty() ? ItemStack.EMPTY : current.copy());
        syncSlotContent(player);
    }

    public static void syncSlotContent(ServerPlayer player) {
        SpyglassSlotData data = Objects.requireNonNullElse(
                player.getData(AttachmentRegistry.HIP_SPYGLASS.get()),
                SpyglassSlotData.EMPTY
        );

        PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                player,
                new SpyglassSlotSyncPacket(player.getId(), data)
        );
    }

}

