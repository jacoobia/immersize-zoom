package com.jacoobia.immersivezoom.mixin;

import com.jacoobia.immersivezoom.client.shader.ZoomShaderHandler;
import com.jacoobia.immersivezoom.common.Constants;
import com.jacoobia.immersivezoom.common.item.AttunedSpyglass;
import com.jacoobia.immersivezoom.common.utils.CollectionUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "isScoping", at = @At("HEAD"), cancellable = true)
    private void modifyIsScoping(CallbackInfoReturnable<Boolean> callback) {
        Player player = (Player) (Object) this;
        ItemStack stack = player.getUseItem();

        if(player.isUsingItem() && CollectionUtils.contains(Constants.SCOPE_ITEMS, stack.getItem())) {
            float zoomLevel = AttunedSpyglass.getZoomLevel(stack);
            ZoomShaderHandler.setZoomFactor(zoomLevel);
            ZoomShaderHandler.enableZoomShader();
            callback.setReturnValue(true);
        } else {
            ZoomShaderHandler.disableZoomShader();
        }
    }

}
