package com.jacoobia.immersivezoom.mixin.client;

import com.jacoobia.immersivezoom.common.item.AttunedSpyglass;
import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import com.jacoobia.immersivezoom.client.utils.LocalZoomState;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.jacoobia.immersivezoom.common.Constants.*;
import static com.jacoobia.immersivezoom.common.registry.DataComponentRegistry.ZOOM_FACTOR;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow private float fov;
    @Shadow private float oldFov;

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void overrideFov(Camera camera, float partialTick, boolean useFovSetting,
                             CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (!immersiveZoom$isZooming()) return;

        double clamped = Mth.clamp(callbackInfoReturnable.getReturnValue(), 0.5f, 179.0f);
        callbackInfoReturnable.setReturnValue(clamped);
    }

    @Inject(method = "tickFov", at = @At("HEAD"), cancellable = true)
    private void tickFov(CallbackInfo callbackInfo) {
        if (!immersiveZoom$isZooming()) return;

        ItemStack active = LocalZoomState.activeSpyglass();

        if (active.getItem() instanceof AttunedSpyglass) {
            Float stored = active.get(ZOOM_FACTOR);
            float magnification = stored != null
                    ? Mth.clamp(stored, ATTUNED_ZOOM_MIN, ATTUNED_ZOOM_MAX)
                    : ATTUNED_ZOOM_DEFAULT;

            this.oldFov = this.fov;
            this.fov += ((1.0f / Math.max(1.0f, magnification)) - this.fov) * ZOOMED_TRANSITION_MODIFIER;
            callbackInfo.cancel();
            return;
        }

        // A vanilla spyglass in hand is already handled by vanilla; only the hotkey needs the zoom
        if (LocalZoomState.getHand() == null) {
            this.oldFov = this.fov;
            this.fov += ((1.0f / VANILLA_ZOOM) - this.fov) * ZOOMED_TRANSITION_MODIFIER;
            callbackInfo.cancel();
        }

    }

    @Unique
    private static boolean immersiveZoom$isZooming() {
        return !ClientUtils.isThirdPerson() && LocalZoomState.isZooming();
    }

}
