package com.jacoobia.immersivezoom.common.registry;

import com.jacoobia.immersivezoom.common.data.SpyglassSlotData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

public class AttachmentRegistry {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);

    public static void init(IEventBus eventBus) {
        ATTACHMENTS.register(eventBus);
    }

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<SpyglassSlotData>> HIP_SPYGLASS =
            ATTACHMENTS.register("hip_spyglass", () ->
                    AttachmentType.builder(() -> SpyglassSlotData.EMPTY).build()
            );

}
