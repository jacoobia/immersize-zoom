package com.jacoobia.immersivezoom;

import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import com.jacoobia.immersivezoom.common.registry.DataComponentRegistry;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import com.jacoobia.immersivezoom.common.registry.LootModifierRegistry;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

import java.time.Duration;
import java.time.Instant;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

@Mod(MOD_ID)
public class ImmersiveZoom {

    private static final Logger LOGGER = LogUtils.getLogger();

    public ImmersiveZoom(IEventBus modEventBus, ModContainer modContainer) {
        Instant start = Instant.now();

        LOGGER.info("[ImmersiveZoom] Registering custom registries...");
        ItemRegistry.init(modEventBus);
        DataComponentRegistry.init(modEventBus);
        LootModifierRegistry.init(modEventBus);
        AttachmentRegistry.init(modEventBus);

        System.out.println("frick");

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        LOGGER.info("[ImmersiveZoom] Mod setup complete in {}ms", duration.toMillis());
    }

}
