package com.jacoobia.immersivezoom.mixin.common;

import com.jacoobia.immersivezoom.common.Constants;
import com.jacoobia.immersivezoom.common.registry.KeyMapRegistry;
import com.jacoobia.immersivezoom.common.utils.CollectionUtils;
import net.minecraft.world.entity.player.Inventory;
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
    private void modifyIsScoping(CallbackInfoReturnable<Boolean> callback) {
        Player player = (Player) (Object) this;

        boolean isUsingSpyglass = player.isUsingItem() &&
                CollectionUtils.contains(Constants.ZOOM_ITEMS, player.getUseItem().getItem());

        boolean hasSpyglassInSlot = !player.getInventory().getItem(Constants.SPYGLASS_SLOT_INDEX).isEmpty() &&
                CollectionUtils.contains(Constants.ZOOM_ITEMS, player.getInventory().getItem(Constants.SPYGLASS_SLOT_INDEX).getItem());

        boolean isHoldingZoomKey = KeyMapRegistry.ZOOM_TOGGLE.isDown();

        callback.setReturnValue(isUsingSpyglass || (isHoldingZoomKey && hasSpyglassInSlot));
    }

}
