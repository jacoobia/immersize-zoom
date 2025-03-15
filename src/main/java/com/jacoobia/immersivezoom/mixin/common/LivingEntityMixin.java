package com.jacoobia.immersivezoom.mixin.common;

import com.jacoobia.immersivezoom.common.Constants;
import com.jacoobia.immersivezoom.common.registry.KeyMapRegistry;
import com.jacoobia.immersivezoom.common.utils.CollectionUtils;
import com.jacoobia.immersivezoom.mixin.accessors.LivingEntityAccessor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    protected ItemStack useItem;

    @Inject(method = "getItemInHand", at = @At("HEAD"), cancellable = true)
    private void getCustomSlotItem(InteractionHand hand, CallbackInfoReturnable<ItemStack> callback) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (hand == null && entity instanceof Player player) {
            ItemStack spyglassItem = player.getInventory().getItem(Constants.SPYGLASS_SLOT_INDEX);
            if(spyglassItem.isEmpty()) return;
            callback.setReturnValue(spyglassItem);
        }
    }

    @Inject(method = "stopUsingItem", at = @At("HEAD"), cancellable = true)
    private void customStopUsingItem(CallbackInfo callback) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof Player player) {
            ItemStack currentUseItem = ((LivingEntityAccessor) player).getUseItem();

            if (!currentUseItem.isEmpty() && CollectionUtils.contains(Constants.ZOOM_ITEMS, useItem.getItem())) {
                if (KeyMapRegistry.ZOOM_TOGGLE.isDown()) {
                    callback.cancel(); // Prevent stopUsingItem from executing
                }
            }
        }
    }

    @Inject(method = "startUsingItem", at = @At("HEAD"), cancellable = true)
    private void customStartUsingItem(InteractionHand hand, CallbackInfo callback) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof Player player) {
            ItemStack spyglassItem = player.getInventory().getItem(Constants.SPYGLASS_SLOT_INDEX);

            if (!spyglassItem.isEmpty() && CollectionUtils.contains(Constants.ZOOM_ITEMS, useItem.getItem())) {
                int duration = 72000;

                LivingEntityAccessor accessor = (LivingEntityAccessor) player;
                accessor.setUseItem(spyglassItem);
                accessor.setUseItemRemaining(duration); // Set a long duration

                if (!player.level().isClientSide) {
                    accessor.invokeSetLivingEntityFlag(1, true);
                    accessor.invokeSetLivingEntityFlag(2, false);
                }

                callback.cancel(); // Prevent vanilla logic from overriding this
            }
        }
    }

}
