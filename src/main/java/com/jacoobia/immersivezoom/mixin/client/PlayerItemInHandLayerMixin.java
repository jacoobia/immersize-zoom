package com.jacoobia.immersivezoom.mixin.client;

import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerItemInHandLayer.class)
public class PlayerItemInHandLayerMixin {

    // Only needed before NeoForge 21.1.201, which checks the SPYGLASS_SCOPE item ability here instead
    // and AttunedSpyglass inherits that from SpyglassItem
    @WrapOperation(method = "renderArmWithItem", require = 0,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean immersiveZoom$attunedUsesSpyglassRender(ItemStack stack, Item item, Operation<Boolean> original) {
        return original.call(stack, item) || stack.is(ItemRegistry.ATTUNED_SPYGLASS.get());
    }
}
