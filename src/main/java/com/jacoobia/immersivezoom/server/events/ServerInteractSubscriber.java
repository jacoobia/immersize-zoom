package com.jacoobia.immersivezoom.server.events;

import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import com.jacoobia.immersivezoom.server.utils.TimeUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerInteractSubscriber {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();
            if(event.getItemStack().is(ItemRegistry.UNATTUNED_AMETHYST_SHARD)) {
                if(!TimeUtils.isAroundMidnight(level) || level.canSeeSkyFromBelowWater(player.blockPosition())) return;

                event.getItemStack().shrink(1);
                if (event.getItemStack().isEmpty()) {
                    player.setItemInHand(event.getHand(), ItemRegistry.ATTUNED_AMETHYST_SHARD.get().getDefaultInstance());
                } else {
                    if (!player.getInventory().add(ItemRegistry.ATTUNED_AMETHYST_SHARD.get().getDefaultInstance())) {
                        player.drop(ItemRegistry.ATTUNED_AMETHYST_SHARD.get().getDefaultInstance(), false);
                    }
                }

                level.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_RESONATE, player.getSoundSource(), 1.0f, 1.0f);

            }
        }
    }

}
