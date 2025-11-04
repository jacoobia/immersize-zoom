package com.jacoobia.immersivezoom.mixin.client;

import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import com.jacoobia.immersivezoom.client.utils.LocalZoomState;
import com.jacoobia.immersivezoom.common.utils.SpyglassUtils;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "isScoping", at = @At("HEAD"), cancellable = true)
    private void modifyIsScoping(CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;

        // Local player: the cached per-tick state, never in third person
        if (player.isLocalPlayer()) {
            cir.setReturnValue(!ClientUtils.isThirdPerson() && LocalZoomState.isZooming());
            return;
        }

        // Everyone else: vanilla's check, widened to include the Attuned Spyglass
        cir.setReturnValue(player.isUsingItem() && SpyglassUtils.isSpyglass(player.getUseItem()));
    }
}
