package com.jacoobia.immersivezoom.mixin.client;

import com.jacoobia.immersivezoom.client.registry.KeyMapRegistry;
import com.jacoobia.immersivezoom.common.item.AttunedSpyglass;
import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import com.jacoobia.immersivezoom.client.utils.ScopedZoomContext;
import com.jacoobia.immersivezoom.common.utils.SpyglassUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
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
        ScopedZoomContext context = immersiveZoom$getScopedZoomContext();
        if (context == null) return;

        double clamped = Mth.clamp(callbackInfoReturnable.getReturnValue(), 0.5f, 179.0f);
        callbackInfoReturnable.setReturnValue(clamped);
    }

    @Inject(method = "tickFov", at = @At("HEAD"), cancellable = true)
    private void tickFov(CallbackInfo callbackInfo) {
        ScopedZoomContext context = immersiveZoom$getScopedZoomContext();
        if (context == null) return;

        boolean isAttuned = context.active().getItem() instanceof AttunedSpyglass;

        if (isAttuned) {
            Float stored = context.active().get(ZOOM_FACTOR);
            float magnification = stored != null
                    ? Mth.clamp(stored, ATTUNED_ZOOM_MIN, ATTUNED_ZOOM_MAX)
                    : ATTUNED_ZOOM_DEFAULT;

            this.oldFov = this.fov;
            this.fov += ((1.0f / Math.max(1.0f, magnification)) - this.fov) * ZOOMED_TRANSITION_MODIFIER;
            callbackInfo.cancel();
            return;
        }

        if (!context.vanillaInHand()) {
            this.oldFov = this.fov;
            this.fov += ((1.0f / VANILLA_ZOOM) - this.fov) * ZOOMED_TRANSITION_MODIFIER;
            callbackInfo.cancel();
        }

    }

    @Unique
    private static ScopedZoomContext immersiveZoom$getScopedZoomContext() {
        LocalPlayer player = ClientUtils.getLocalPlayerCast();
        if (player == null || ClientUtils.isThirdPerson()) return null;

        boolean vanillaInHand = immersiveZoom$isVanillaInHandScoping(player);
        boolean hotkeyInSlot  = immersiveZoom$isHotkeySlotScoping(player);
        if (!vanillaInHand && !hotkeyInSlot) return null;

        ItemStack active = vanillaInHand
                ? player.getUseItem()
                : player.getInventory().getItem(SPYGLASS_SLOT_INDEX);

        if (active.isEmpty() || !SpyglassUtils.isSpyglass(active)) return null;

        return new ScopedZoomContext(player, active, vanillaInHand);
    }

    @Unique
    private static boolean immersiveZoom$isVanillaInHandScoping(LocalPlayer player) {
        return player.isUsingItem() && SpyglassUtils.isSpyglass(player.getUseItem());
    }

    @Unique
    private static boolean immersiveZoom$isHotkeySlotScoping(LocalPlayer player) {
        if (!KeyMapRegistry.ZOOM_KEYMAPPING.isDown()) return false;
        ItemStack slot = player.getInventory().getItem(SPYGLASS_SLOT_INDEX);
        return SpyglassUtils.isSpyglass(slot);
    }

}
