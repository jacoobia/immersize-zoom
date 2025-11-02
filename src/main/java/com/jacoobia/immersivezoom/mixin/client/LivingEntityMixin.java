package com.jacoobia.immersivezoom.mixin.client;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.jacoobia.immersivezoom.common.Constants.SPYGLASS_SLOT_INDEX;
import static com.jacoobia.immersivezoom.client.registry.KeyMapRegistry.ZOOM_KEYMAPPING;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "stopUsingItem", at = @At("HEAD"), cancellable = true)
    private void preventStopUsingItemWhenZooming(CallbackInfo callback) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if(!(entity instanceof Player player)) return;
        if (immersiveZoom$shouldKeepUsingItem(player)) {
            callback.cancel();
        }
    }

    @Unique
    private boolean immersiveZoom$shouldKeepUsingItem(Player player) {
        if(!player.isLocalPlayer() || !player.isScoping()) return false;
        return ZOOM_KEYMAPPING.isDown() && !player.getInventory().getItem(SPYGLASS_SLOT_INDEX).isEmpty();
    }

}
