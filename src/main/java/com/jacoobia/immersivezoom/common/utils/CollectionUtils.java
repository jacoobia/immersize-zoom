package com.jacoobia.immersivezoom.common.utils;

public class CollectionUtils {

    public static boolean contains(Object[] array, Object obj) {
        for (Object o : array) {
            if (o.equals(obj)) {
                return true;
            }
        }
        return false;
    }

}
