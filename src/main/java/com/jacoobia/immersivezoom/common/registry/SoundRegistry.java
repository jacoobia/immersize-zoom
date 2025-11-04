package com.jacoobia.immersivezoom.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

public class SoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);

    public static void init(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }

    public static final DeferredHolder<SoundEvent, SoundEvent> HUM = SOUNDS.register(
            "hum",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "hum"))
    );

}
