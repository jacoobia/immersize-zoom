package com.jacoobia.immersivezoom.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.jacoobia.immersivezoom.common.Constants.CUSTOM_INVENTORY_TEXTURE;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    @Final
    @Shadow
    @Mutable
    public static ResourceLocation INVENTORY_LOCATION;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void replaceInventoryTexture(CallbackInfo callback) {
        INVENTORY_LOCATION = CUSTOM_INVENTORY_TEXTURE;
    }

}
