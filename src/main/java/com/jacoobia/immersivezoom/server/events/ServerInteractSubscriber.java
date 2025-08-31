package com.jacoobia.immersivezoom.server.events;

import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import com.jacoobia.immersivezoom.client.utils.ParticleUtil;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import com.jacoobia.immersivezoom.common.utils.NumberUtils;
import com.jacoobia.immersivezoom.server.utils.TimeUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;
import static com.jacoobia.immersivezoom.common.utils.NumberUtils.FLOAT_ONE;
import static com.jacoobia.immersivezoom.common.utils.NumberUtils.INT_ONE;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerInteractSubscriber {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();
            if(event.getItemStack().is(ItemRegistry.UNATTUNED_AMETHYST_SHARD)) {
                if(!TimeUtils.isAroundMidnight(level) || !level.canSeeSkyFromBelowWater(player.blockPosition())) return;

                Vec3 playerPos = ClientUtils.getEstimatedHandPosition(player);
                level.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, player.getSoundSource(), FLOAT_ONE, FLOAT_ONE);

                ParticleUtil.playMysticalEffect(level, playerPos, ParticleTypes.END_ROD, () -> {
                    event.getItemStack().shrink(INT_ONE);
                    player.drop(ItemRegistry.ATTUNED_AMETHYST_SHARD.get().getDefaultInstance(), false);
                    level.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_RESONATE, player.getSoundSource(), FLOAT_ONE, FLOAT_ONE);
                });
            }
        }
    }

}
