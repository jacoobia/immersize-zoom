package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.DataTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientUIEventsSubscriber {

    @SubscribeEvent
    public static void onRenderHUD(RenderGuiEvent.Post event) {
        GuiGraphics gui = event.getGuiGraphics();
        DataTracker.drawHud(gui);
    }

}
