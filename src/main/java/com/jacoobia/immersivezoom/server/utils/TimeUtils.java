package com.jacoobia.immersivezoom.server.utils;

import net.minecraft.server.level.ServerLevel;

public class TimeUtils {

    private static final long TICKS_PER_DAY = 24000L;
    private static final long MIDNIGHT = 18000L;
    private static final long TOLERANCE = 500L;

    public static boolean isAroundMidnight(ServerLevel level) {
        if (!level.dimensionType().hasSkyLight()) return false;
        long time = Math.floorMod(level.getDayTime(), TICKS_PER_DAY);
        long diff = Math.abs(time - MIDNIGHT);
        diff = Math.min(diff, TICKS_PER_DAY - diff);
        return diff <= TOLERANCE;
    }
}
