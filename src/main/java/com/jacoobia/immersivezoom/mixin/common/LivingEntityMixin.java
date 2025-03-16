package com.jacoobia.immersivezoom.mixin.common;

import com.jacoobia.immersivezoom.common.Constants;
import com.jacoobia.immersivezoom.common.registry.KeyMapRegistry;
import com.jacoobia.immersivezoom.common.utils.CollectionUtils;
import com.jacoobia.immersivezoom.common.utils.SpyglassUtils;
import com.jacoobia.immersivezoom.mixin.accessors.LivingEntityAccessor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.jacoobia.immersivezoom.common.registry.KeyMapRegistry.ZOOM_TOGGLE;

@SuppressWarnings("resource")
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

    @Inject(method = "startUsingItem", at = @At("HEAD"), cancellable = true)
    private void customStartUsingItem(InteractionHand hand, CallbackInfo callback) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof Player player) {
            if(hand == null) {
                ItemStack spyglassItem = player.getInventory().getItem(Constants.SPYGLASS_SLOT_INDEX);

                if (!spyglassItem.isEmpty() && SpyglassUtils.isSpyglass(spyglassItem)) {
                    LivingEntityAccessor accessor = (LivingEntityAccessor) player;
                    accessor.setUseItem(spyglassItem);

                    if (!player.level().isClientSide) {
                        accessor.invokeSetLivingEntityFlag(1, true);
                        accessor.invokeSetLivingEntityFlag(2, false);
                    }

                    callback.cancel();
                }

            } else {
                // TODO: Prevent this earlier as it currently starts to use the item before the event is cancelled
                if(player.isScoping()) {
                    callback.cancel();
                }
            }
        }
    }

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
        if(!player.isScoping()) return false;
        return ZOOM_TOGGLE.isDown() && !player.getInventory().getItem(Constants.SPYGLASS_SLOT_INDEX).isEmpty();
    }

}
