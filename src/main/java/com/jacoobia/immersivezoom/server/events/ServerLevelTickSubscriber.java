package com.jacoobia.immersivezoom.server.events;

import com.jacoobia.immersivezoom.server.utils.MidnightCheck;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerLevelTickSubscriber {

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        if (level.dimension() == Level.OVERWORLD) {
            MidnightCheck.tickOverworld(level);
        }
    }

}
