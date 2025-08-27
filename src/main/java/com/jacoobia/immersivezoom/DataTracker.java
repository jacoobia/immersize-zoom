package com.jacoobia.immersivezoom;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;


import com.jacoobia.immersivezoom.mixin.client.GameRendererInvoker;
import net.minecraft.client.renderer.GameRenderer;

public class DataTracker {

    private static final int TEXT_HEIGHT = 5;
    private static final int BASE_X = 10;
    private static final int BASE_Y = 10;

    public static void drawHud(GuiGraphics graphics) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) return;

        GameRenderer gameRenderer = minecraft.gameRenderer;
        Camera camera = gameRenderer.getMainCamera();
        float partialTick = minecraft.getFrameTimeNs();

        double fovNow = ((GameRendererInvoker) gameRenderer)
                .immersiveZoom$invokeGetFov(camera, partialTick, true);

        graphics.pose().pushPose();
        graphics.pose().scale(1.0f, 1.0f, 1.0f);

        String fovStr = String.format(java.util.Locale.ROOT, "%.2f", fovNow);
        graphics.drawString(minecraft.font, "Current FOV: " + fovStr,
                BASE_X, BASE_Y + TEXT_HEIGHT, 0xFFFFFF);

        graphics.pose().popPose();
    }
}


