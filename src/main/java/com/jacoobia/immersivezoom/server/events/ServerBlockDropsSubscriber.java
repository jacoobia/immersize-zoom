package com.jacoobia.immersivezoom.server.events;

import com.jacoobia.immersivezoom.common.Translations;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;
import static org.apache.commons.lang3.math.NumberUtils.FLOAT_ONE;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerBlockDropsSubscriber {

    @SubscribeEvent
    public static void onBlockDropItem(BlockDropsEvent event) {
        if(!(event.getBreaker() instanceof ServerPlayer player)) return;
        for(ItemEntity droppedItem : event.getDrops()) {
            ServerLevel level = player.serverLevel();
            if(droppedItem.getItem().is(ItemRegistry.UNATTUNED_AMETHYST_SHARD)) {
                level.playSound(null, player.blockPosition(),
                        SoundEvents.AMETHYST_BLOCK_BREAK, player.getSoundSource(), FLOAT_ONE, FLOAT_ONE);
                player.displayClientMessage(Translations.Lore.mined, true);
            }
        }
    }

}
