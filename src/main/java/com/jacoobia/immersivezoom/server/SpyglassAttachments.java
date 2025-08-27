package com.jacoobia.immersivezoom.server;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

public class SpyglassAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ServerSpyglassState>> SPYGLASS_STATE =
            ATTACHMENTS.register("spyglass_state",
                    () -> AttachmentType.serializable(ServerSpyglassState::new).build());

    public static void init(IEventBus bus) {
        ATTACHMENTS.register(bus);
    }

}
