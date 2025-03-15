package com.jacoobia.immersivezoom.mixin.common;

import com.jacoobia.immersivezoom.common.Constants;
import com.jacoobia.immersivezoom.common.utils.CollectionUtils;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "isScoping", at = @At("HEAD"), cancellable = true)
    private void modifyIsScoping(CallbackInfoReturnable<Boolean> callback) {
        Player player = (Player) (Object) this;
        if(player.isUsingItem() && CollectionUtils.contains(Constants.ZOOM_ITEMS, player.getUseItem().getItem())) {
            callback.setReturnValue(true);
        }
    }

}
