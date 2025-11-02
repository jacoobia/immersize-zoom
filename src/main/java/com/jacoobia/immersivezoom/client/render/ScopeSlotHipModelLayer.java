package com.jacoobia.immersivezoom.client.render;

import com.jacoobia.immersivezoom.client.registry.KeyMapRegistry;
import com.jacoobia.immersivezoom.client.temp.HipTuner;
import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import com.jacoobia.immersivezoom.common.utils.HipSide;
import com.jacoobia.immersivezoom.common.utils.SpyglassType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class ScopeSlotHipModelLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public ScopeSlotHipModelLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    @Override
    public void render(@NotNull PoseStack pose, @NotNull MultiBufferSource buffers, int light,
                       @NotNull AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if (!AttachmentRegistry.HIP_SPYGLASS.isBound()) return;

        var data = player.getData(AttachmentRegistry.HIP_SPYGLASS.get());
        if (data.spyglassType() == SpyglassType.NONE) return;

        if (player.isLocalPlayer() && Minecraft.getInstance().options.getCameraType().isFirstPerson()) return;

        Item item = (data.spyglassType() == SpyglassType.ATTUNED)
                ? ItemRegistry.ATTUNED_SPYGLASS.get()
                : Items.SPYGLASS;
        ItemStack stack = new ItemStack(item);
        if (stack.isEmpty()) return;

        if (player.isLocalPlayer() && KeyMapRegistry.ZOOM_KEYMAPPING.isDown()) {
            pose.pushPose();

            var arm = (data.side() == HipSide.LEFT) ? HumanoidArm.LEFT : HumanoidArm.RIGHT;

            this.getParentModel().translateToHand(arm, pose);

            pose.mulPose(Axis.XP.rotationDegrees(-90.0F));
            pose.mulPose(Axis.YP.rotationDegrees(180.0F));

            boolean left = (arm == HumanoidArm.LEFT);
            pose.translate(left ? -1.0F / 16.0F : 1.0F / 16.0F, 0.125F, -0.625F);

            var ctx = left ? ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                    : ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    stack, ctx, light, NO_OVERLAY, pose, buffers, player.level(), player.getId()
            );

            pose.popPose();
            return;
        }

        pose.pushPose();
        this.getParentModel().body.translateAndRotate(pose);

        double bx = HipTuner.x(), by = HipTuner.y(), bz = HipTuner.z();
        double x = (data.side() == HipSide.RIGHT) ? -bx : bx;
        pose.translate(x, by, bz);

        pose.mulPose(Axis.YP.rotationDegrees(data.side() == HipSide.RIGHT ? 90f : -90f));
        pose.mulPose(Axis.ZP.rotationDegrees(data.side() == HipSide.RIGHT ? -10f : 10f));
        pose.scale(0.8f, 0.4f, 0.8f);

        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack,
                ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                light,
                NO_OVERLAY,
                pose,
                buffers,
                player.level(),
                player.getId()
        );

        pose.popPose();
    }
}
