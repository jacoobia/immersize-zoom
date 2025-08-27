package com.jacoobia.immersivezoom.common.utils;

public class NumberUtils {

    public static final float FLOAT_ZERO = 0.0f;

    /**
     * Check if the given ItemStack is a spyglass
     * @param current The ItemStack to check
     * @param min The minimum value of the range
     * @param max The maximum value of the range
     * @return The percentage of current between min and max
     */
    public static double calculatePercentage(double current, double min, double max) {
        return 100 - ((current - max) / (min - max)) * 100;
    }

    /**
     * Calculate the rounded percentage of current between min and max
     * @param current the current value
     * @param min the minimum value
     * @param max the maximum value
     * @return The rounded percentage of current between min and max
     */
    public static double roundedPercentage(double current, double min, double max) {
        return Math.round(calculatePercentage(current, min, max));
    }

}
