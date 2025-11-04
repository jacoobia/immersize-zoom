package com.jacoobia.immersivezoom;

import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import com.jacoobia.immersivezoom.common.registry.DataComponentRegistry;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import com.jacoobia.immersivezoom.common.registry.LootModifierRegistry;
import com.jacoobia.immersivezoom.common.registry.SoundRegistry;
import lombok.extern.slf4j.Slf4j;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import java.time.Duration;
import java.time.Instant;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

@Slf4j
@Mod(MOD_ID)
public class ImmersiveZoom {

    public ImmersiveZoom(IEventBus modEventBus, ModContainer modContainer) {
        Instant start = Instant.now();

        log.info("[ImmersiveZoom] Registering custom registries...");
        ItemRegistry.init(modEventBus);
        DataComponentRegistry.init(modEventBus);
        LootModifierRegistry.init(modEventBus);
        AttachmentRegistry.init(modEventBus);
        SoundRegistry.init(modEventBus);

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        log.info("[ImmersiveZoom] Setup complete in {}ms", duration.toMillis());
    }

}
