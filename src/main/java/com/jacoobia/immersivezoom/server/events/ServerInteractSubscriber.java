package com.jacoobia.immersivezoom.server.events;

import com.jacoobia.immersivezoom.common.Translations;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import com.jacoobia.immersivezoom.server.utils.ritual.AttunementRitual;
import com.jacoobia.immersivezoom.server.utils.TimeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;
import static com.jacoobia.immersivezoom.common.utils.NumberUtils.INT_ONE;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerInteractSubscriber {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();
            ItemStack itemStack = event.getItemStack();

            if(itemStack.is(ItemRegistry.UNATTUNED_AMETHYST_SHARD)) {
                if(ritualNotReady(level, player.blockPosition())) return;
                if(AttunementRitual.isRunningFor(player)) return;

                player.displayClientMessage(Translations.Lore.attuneLore, true);
                AttunementRitual.start(level, player, itemStack.split(INT_ONE));

                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }

    private static boolean ritualNotReady(ServerLevel level, BlockPos position) {
        return !TimeUtils.isAroundMidnight(level) || !level.canSeeSkyFromBelowWater(position);
    }

}
