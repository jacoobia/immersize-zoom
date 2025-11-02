package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.client.ui.tooltip.AttunedZoomBarClient;
import com.jacoobia.immersivezoom.common.data.AttunedZoomBarTooltip;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientUIRegistrySubscriber {

    @SubscribeEvent
    public static void register(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(AttunedZoomBarTooltip.class, AttunedZoomBarClient::new);
    }

}
