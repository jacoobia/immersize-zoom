package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.client.render.ScopeSlotHipModelLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;
import static net.minecraft.client.resources.PlayerSkin.Model.SLIM;
import static net.minecraft.client.resources.PlayerSkin.Model.WIDE;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEntityRenderSubscriber {

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers evt) {
        PlayerRenderer steve = evt.getSkin(WIDE);
        PlayerRenderer alex  = evt.getSkin(SLIM);
        if (steve != null)
            steve.addLayer(new ScopeSlotHipModelLayer(steve));
        if (alex  != null)
            alex.addLayer(new ScopeSlotHipModelLayer(alex));
    }

}
