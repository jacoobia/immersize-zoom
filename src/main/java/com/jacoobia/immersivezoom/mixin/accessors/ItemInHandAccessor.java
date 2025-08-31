package com.jacoobia.immersivezoom.mixin.accessors;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemInHandRenderer.class)
public interface ItemInHandAccessor {

    @Invoker("renderArmWithItem")
    void callRenderArmWithItem(AbstractClientPlayer player, float partialTick, float pitch,
                               InteractionHand hand, float swing, ItemStack stack, float equip,
                               PoseStack pose, MultiBufferSource buffers, int light);

    @Invoker("renderPlayerArm")
    void callRenderPlayerArm(PoseStack pose, MultiBufferSource buffers, int light,
                             float equip, float swing, HumanoidArm arm);

}
