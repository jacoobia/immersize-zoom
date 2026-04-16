package com.jacoobia.immersivezoom.server.events;

import com.jacoobia.immersivezoom.server.utils.MidnightCheck;
import com.jacoobia.immersivezoom.server.utils.ritual.AttunementRitual;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerLifecycleSubscriber {

    /**
     * Clears server state so nothing carries over between singleplayer worlds
     * @param event the ServerStoppedEvent
     */
    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        AttunementRitual.reset();
        MidnightCheck.reset();
    }

}
