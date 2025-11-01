package com.jacoobia.immersivezoom.common.registry;

import com.jacoobia.immersivezoom.common.network.Codec.CustomCodecs;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

public class DataComponentRegistry {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MOD_ID);

    public static void init(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }

    public static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return DATA_COMPONENT_TYPES.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> ZOOM_FACTOR = register(
            "zoom_factor",
            builder -> builder
                    .persistent(ExtraCodecs.POSITIVE_FLOAT)
                    .networkSynchronized(ByteBufCodecs.FLOAT)
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TESTER = register(
            "tester",
            builder -> builder
                    .persistent(ExtraCodecs.NON_NEGATIVE_INT)
                    .networkSynchronized(ByteBufCodecs.INT)
    );

}
