package com.jacoobia.immersivezoom.client.temp;

public final class HipTuner {

    private static double x = 0.1;
    private static double y = 0.7;
    private static double z = 0.15;

    public static double x() { return x; }
    public static double y() { return y; }
    public static double z() { return z; }

    public static void set(double nx, double ny, double nz) { x = nx; y = ny; z = nz; }
    public static void setX(double nx) { x = nx; }
    public static void setY(double ny) { y = ny; }
    public static void setZ(double nz) { z = nz; }

}
