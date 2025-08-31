package com.jacoobia.immersivezoom.client.temp;

import com.jacoobia.immersivezoom.client.registry.KeyMapRegistry;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class Render {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post e) {
        if (KeyMapRegistry.TOGGLE_TEMP_SCREEN.consumeClick()) {
            Minecraft.getInstance().setScreen(new HipTunerScreen());
        }
    }

}
