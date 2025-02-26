package com.jacoobia.immersivezoom.client.shader;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

public class ZoomShaderHandler {

    public static final ResourceLocation SHADER_RESOURCE =
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "shaders/post/zoom");

    private static ShaderInstance zoomShader;
    private static float zoomFactor = 1.0f;

    public static void setZoomShader(ShaderInstance shader) {
        zoomShader = shader;
    }

    public static ShaderInstance getZoomShader() {
        return zoomShader;
    }

    /**
     * Enables the zoom shader and applies the zoom factor.
     */
    public static void enableZoomShader() {
        if (zoomShader != null) {
            setZoomFactor(zoomFactor);
//            zoomShader.apply();

            RenderSystem.setShader(() -> zoomShader);
        }
    }

    /**
     * Disables the zoom shader when the spyglass is no longer used.
     */
    public static void disableZoomShader() {
        RenderSystem.setShader(() -> null);
    }

    /**
     * Sets the zoom factor and updates the shader uniform properly.
     */
    public static void setZoomFactor(float factor) {
        zoomFactor = factor;

        if (zoomShader != null) {
            // Retrieve the uniform variable from the shader
            Uniform zoomUniform = zoomShader.getUniform("ZoomFactor");
            if (zoomUniform != null) {
                zoomUniform.set(zoomFactor);
            }
        }
    }
}
