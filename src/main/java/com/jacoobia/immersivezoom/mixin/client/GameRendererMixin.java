package com.jacoobia.immersivezoom.mixin.client;

import com.jacoobia.immersivezoom.common.item.AttunedSpyglass;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.NumberUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.jacoobia.immersivezoom.common.Constants.MAX_FOV;
import static com.jacoobia.immersivezoom.common.Constants.MIN_FOV;
import static com.jacoobia.immersivezoom.common.Constants.ZOOMED_TRANSITION_MODIFIER;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    private float fov;
    @Shadow
    private float oldFov;

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void getFov(Camera activeRenderInfo, float partialTicks, boolean useFOVSetting, CallbackInfoReturnable<Double> callback) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;

        if (localPlayer != null && localPlayer.isUsingItem()) {
            ItemStack stack = localPlayer.getUseItem();

            if (stack.is(ItemRegistry.ATTUNED_SPYGLASS.get())) {
                double normalFov = callback.getReturnValue();
                float zoomLevel = AttunedSpyglass.getZoomLevel(stack);

                if (normalFov > MAX_FOV) normalFov = MAX_FOV;
                if (normalFov < MIN_FOV) normalFov = MIN_FOV;

                double modifiedFov = normalFov / zoomLevel;

                if (modifiedFov > MAX_FOV) modifiedFov = MAX_FOV;
                if (modifiedFov < MIN_FOV) modifiedFov = MIN_FOV;

                callback.setReturnValue(modifiedFov);
            }
        }
    }

    @Inject(method = "tickFov", at = @At("HEAD"), cancellable = true)
    private void tickFov(CallbackInfo callback) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;

        if (localPlayer != null && localPlayer.isUsingItem()) {
            ItemStack stack = localPlayer.getUseItem();

            if (stack.is(ItemRegistry.ATTUNED_SPYGLASS.get())) {
                callback.cancel();

                float zoomLevel = AttunedSpyglass.getZoomLevel(stack);

                this.oldFov = this.fov;
                this.fov += ((NumberUtils.FLOAT_ONE / zoomLevel) - this.fov) * ZOOMED_TRANSITION_MODIFIER;
            }
        }
    }

}
