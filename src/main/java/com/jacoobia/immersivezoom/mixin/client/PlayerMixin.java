package com.jacoobia.immersivezoom.mixin.client;

import com.jacoobia.immersivezoom.client.registry.KeyMapRegistry;
import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import com.jacoobia.immersivezoom.common.utils.SpyglassUtils;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.jacoobia.immersivezoom.common.Constants.SPYGLASS_SLOT_INDEX;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "isScoping", at = @At("HEAD"), cancellable = true)
    private void modifyIsScoping(CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;

        if (player.isLocalPlayer() && ClientUtils.isThirdPerson()) {
            cir.setReturnValue(false);
            cir.cancel();
            return;
        }

        boolean isUsingSpyglassInHand =
                player.isUsingItem() && SpyglassUtils.isSpyglass(player.getUseItem());

        boolean hasSpyglassInSpecialSlot =
                SpyglassUtils.isSpyglass(player.getInventory().getItem(SPYGLASS_SLOT_INDEX));

        boolean isZoomHotkeyHeld = player.isLocalPlayer() && KeyMapRegistry.ZOOM_KEYMAPPING.isDown();

        boolean shouldScope = isUsingSpyglassInHand || (isZoomHotkeyHeld && hasSpyglassInSpecialSlot);

        cir.setReturnValue(shouldScope);
        cir.cancel();
    }
}
