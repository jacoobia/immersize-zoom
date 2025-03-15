package com.jacoobia.immersivezoom.mixin.client;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Zooming is always classed as 'first person' since we're using a spyglass!
 */
@Mixin(CameraType.class)
public class CameraTypeMixin {

    @Inject(method = "isFirstPerson", at = @At("HEAD"), cancellable = true)
    private void modifyFirstPerson(CallbackInfoReturnable<Boolean> callback) {
        Minecraft minecraft = Minecraft.getInstance();
        assert minecraft.player != null;
        Player player = minecraft.player;

        if(player.isScoping() && !minecraft.options.getCameraType().isMirrored()) {
            callback.setReturnValue(true);
        }
    }

}
