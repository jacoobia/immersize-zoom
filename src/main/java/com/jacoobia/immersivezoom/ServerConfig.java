package com.jacoobia.immersivezoom;

import com.jacoobia.immersivezoom.common.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ServerConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ENABLE_ATTUNED_SPYGLASS = BUILDER.comment("Enables/Disables the ability to craft and use the attuned spyglass.").define("enableAttunedSpyglass", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean enableAttunedSpyglass;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if(event.getConfig().getType() != ModConfig.Type.SERVER) return;
        enableAttunedSpyglass = ENABLE_ATTUNED_SPYGLASS.get();
    }
}
