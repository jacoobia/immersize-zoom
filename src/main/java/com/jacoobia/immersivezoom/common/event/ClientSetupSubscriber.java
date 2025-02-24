package com.jacoobia.immersivezoom.common.event;

import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;
import static org.apache.commons.lang3.math.NumberUtils.FLOAT_ONE;
import static org.apache.commons.lang3.math.NumberUtils.FLOAT_ZERO;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetupSubscriber {

    private static final String USING_RESOURCE_LOCATION = "using";

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ItemRegistry.ATTUNED_SPYGLASS.get(),
                    ResourceLocation.withDefaultNamespace(USING_RESOURCE_LOCATION),
                    (stack, world, entity, seed) ->
                            (entity != null && entity.isUsingItem() && entity.getUseItem() == stack) ? FLOAT_ONE : FLOAT_ZERO
            );
        });
    }

}
