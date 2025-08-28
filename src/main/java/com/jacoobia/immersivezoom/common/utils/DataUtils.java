package com.jacoobia.immersivezoom.common.utils;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class DataUtils {

    /**
     * Gets a float value from the ItemStack's data component, or returns the default value if not present.
     * @param itemStack the ItemStack to retrieve the data from
     * @param type the DataComponentType supplier
     * @param defaultValue the default value to return if the data is not present
     * @return the float value from the data component, or the default value
     * @param <T> the type of the data component
     */
    public static <T> float getFloatOrDefault(ItemStack itemStack, Supplier<? extends DataComponentType<? extends T>> type, float defaultValue) {
        Float value = (Float) itemStack.get(type.get());
        return value != null ? value : defaultValue;
    }

    /**
     * Gets an int value from the ItemStack's data component, or returns the default value if not present.
     * @param itemStack the ItemStack to retrieve the data from
     * @param type the DataComponentType supplier
     * @param defaultValue the default value to return if the data is not present
     * @return the int value from the data component, or the default value
     * @param <T> the type of the data component
     */
    public static <T> int getIntOrDefault(ItemStack itemStack, Supplier<? extends DataComponentType<? extends T>> type, int defaultValue) {
        Integer value = (Integer) itemStack.get(type.get());
        return value != null ? value : defaultValue;
    }

}
