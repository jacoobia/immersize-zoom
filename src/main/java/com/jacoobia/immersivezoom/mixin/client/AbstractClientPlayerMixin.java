package com.jacoobia.immersivezoom.mixin.client;

import com.jacoobia.immersivezoom.common.item.AttunedSpyglass;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.jacoobia.immersivezoom.common.Constants.MAX_ZOOM;

/**
 * A mixin to set the FOV value based on the value of the used spyglass
 */
@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {

    @Inject(method = "getFieldOfViewModifier", at = @At("HEAD"), cancellable = true)
    private void modifyFOV(CallbackInfoReturnable<Float> callback) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        assert localPlayer != null;

        ItemStack stack = localPlayer.getUseItem();

        if (localPlayer.isUsingItem() && stack.is(ItemRegistry.ATTUNED_SPYGLASS.get())) {
            float zoomLevel = AttunedSpyglass.getZoomLevel(stack);
            float adjustedFOV = Math.max(zoomLevel, MAX_ZOOM);
            callback.setReturnValue(adjustedFOV);
        }
    }

}
