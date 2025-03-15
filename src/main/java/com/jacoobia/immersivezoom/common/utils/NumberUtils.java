package com.jacoobia.immersivezoom.common.utils;

public class NumberUtils {

    public static double calculatePercentage(double current, double min, double max) {
        return ((current - max) / (min - max)) * 100;
    }

}
