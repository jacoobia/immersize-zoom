package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.client.config.ClientConfig;
import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientConfigSubscriber {

    /**
     * Updates belt placement on config change, no relog needed :)
     */
    @SubscribeEvent
    public static void onConfigReloading(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() != ClientConfig.SPEC) return;
        Minecraft.getInstance().execute(ClientUtils::sendBeltPlacement);
    }

}
