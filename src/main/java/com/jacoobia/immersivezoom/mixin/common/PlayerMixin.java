package com.jacoobia.immersivezoom.mixin.common;

import com.jacoobia.immersivezoom.client.registry.KeyMapRegistry;
import com.jacoobia.immersivezoom.common.Constants;
import com.jacoobia.immersivezoom.common.utils.ClientUtils;
import com.jacoobia.immersivezoom.common.utils.CollectionUtils;
import com.jacoobia.immersivezoom.common.utils.SpyglassUtils;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.jacoobia.immersivezoom.common.Constants.SPYGLASS_SLOT_INDEX;
import static com.jacoobia.immersivezoom.common.Constants.ZOOM_ITEMS;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "isScoping", at = @At("HEAD"), cancellable = true)
    private void modifyIsScoping(CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;

        // No zoom/overlay in third-person FRONT
        if (ClientUtils.isThirdPersonFront()) {
            cir.setReturnValue(false);
            cir.cancel();
            return;
        }

        boolean isUsingSpyglassInHand =
                player.isUsingItem() && SpyglassUtils.isSpyglass(player.getUseItem());

        boolean hasSpyglassInSpecialSlot =
                SpyglassUtils.isSpyglass(player.getInventory().getItem(SPYGLASS_SLOT_INDEX));

        boolean isZoomHotkeyHeld = KeyMapRegistry.ZOOM_KEYMAPPING.isDown();

        boolean shouldScope = isUsingSpyglassInHand || (isZoomHotkeyHeld && hasSpyglassInSpecialSlot);

        cir.setReturnValue(shouldScope);
        cir.cancel();
    }
}
