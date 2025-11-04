package com.jacoobia.immersivezoom.server.events;

import com.jacoobia.immersivezoom.common.utils.ParticleUtil;
import com.jacoobia.immersivezoom.server.utils.MidnightCheck;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerTickSubscriber {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        ParticleUtil.tickEffects();
        MidnightCheck.tickArrivals(event.getServer());
    }

}
