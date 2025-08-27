package com.jacoobia.immersivezoom.common.utils;

public class CollectionUtils {

    /**
     * Check if an array contains a specific object
     * @param array the array to check
     * @param obj the object to check for
     * @return True if the array contains the object, false otherwise
     */
    public static boolean contains(Object[] array, Object obj) {
        for (Object o : array) {
            if (o.equals(obj)) {
                return true;
            }
        }
        return false;
    }

}
