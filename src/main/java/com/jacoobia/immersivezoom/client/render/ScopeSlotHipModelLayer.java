package com.jacoobia.immersivezoom.client.render;

import com.jacoobia.immersivezoom.client.temp.HipTuner;
import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import com.jacoobia.immersivezoom.common.utils.HipSide;
import com.jacoobia.immersivezoom.common.utils.SpyglassType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

        if (ClientUtils.isHotkeyScoping(player)) {
            pose.pushPose();

            ModelPart head = this.getParentModel().getHead();
            float xRot = head.xRot;
            head.xRot = Mth.clamp(head.xRot, (float) (-Math.PI / 6), (float) (Math.PI / 2));
            head.translateAndRotate(pose);
            head.xRot = xRot;

            CustomHeadLayer.translateToHead(pose, false);
            boolean left = (data.side() == HipSide.LEFT);
            pose.translate((left ? -2.5F : 2.5F) / 16.0F, -0.0625F, 0.0F);

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    stack, ItemDisplayContext.HEAD, light, NO_OVERLAY, pose, buffers, player.level(), player.getId()
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
