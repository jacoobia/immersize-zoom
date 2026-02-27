package com.jacoobia.immersivezoom.server.events;

import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

/**
 * Drops the spyglass slot's item on death like the rest of the inventory
 */
@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerDeathSubscriber {

    private static final double DROP_BELOW_EYES = 0.3;
    private static final int DROP_PICKUP_DELAY = 40;

    @SubscribeEvent
    public static void onDrops(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) return;

        ItemStack stack = player.getData(AttachmentRegistry.SPYGLASS_SLOT);
        if (stack.isEmpty()) return;
        player.setData(AttachmentRegistry.SPYGLASS_SLOT, ItemStack.EMPTY);

        ItemEntity drop = new ItemEntity(player.level(), player.getX(), player.getEyeY() - DROP_BELOW_EYES, player.getZ(), stack);
        drop.setPickUpDelay(DROP_PICKUP_DELAY);
        event.getDrops().add(drop);
    }

}
