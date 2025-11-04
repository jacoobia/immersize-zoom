package com.jacoobia.immersivezoom.client.render;

import com.jacoobia.immersivezoom.client.config.ClientConfig;
import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

/**
 * Key for my sanity lol
 * +X = left
 * -X = right
 * +Z = back
 * -Z = front
 */
public class ScopeSlotHipModelLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private static final double HIP_OFFSET_Y = 0.7;
    private static final double BACK_HIP_OFFSET_X = 0.1;
    private static final double BACK_HIP_OFFSET_Z = 0.15;
    private static final double FRONT_HIP_OFFSET_X = 0.13;
    private static final double FRONT_HIP_OFFSET_Z = -0.2;
    private static final double SIDE_HIP_OFFSET_X = 0.25;
    private static final double SIDE_HIP_OFFSET_Z = 0.0;

    private final ItemStack spyglassStack = new ItemStack(Items.SPYGLASS);
    private final ItemStack attunedSpyglassStack = new ItemStack(ItemRegistry.ATTUNED_SPYGLASS.get());

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

        ItemStack stack = (data.spyglassType() == SpyglassType.ATTUNED) ? attunedSpyglassStack : spyglassStack;

        if (ClientUtils.isHotkeyScoping(player)) {
            pose.pushPose();

            ModelPart head = this.getParentModel().getHead();
            float xRot = head.xRot;
            head.xRot = Mth.clamp(head.xRot, (float) (-Math.PI / 6), (float) (Math.PI / 2));
            head.translateAndRotate(pose);
            head.xRot = xRot;

            CustomHeadLayer.translateToHead(pose, false);
            pose.translate((data.placement().isLeft() ? 2.5F : -2.5F) / 16.0F, -0.0625F, 0.0F);

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    stack, ItemDisplayContext.HEAD, light, NO_OVERLAY, pose, buffers, player.level(), player.getId()
            );

            pose.popPose();
            return;
        }

        if (!data.visible() || !ClientConfig.RENDER_BELT_SPYGLASSES.get()) return;

        pose.pushPose();
        this.getParentModel().body.translateAndRotate(pose);

        boolean left = data.placement().isLeft();
        double offsetX = switch (data.placement()) {
            case FRONT_LEFT, FRONT_RIGHT -> FRONT_HIP_OFFSET_X;
            case SIDE_LEFT, SIDE_RIGHT -> SIDE_HIP_OFFSET_X;
            case BACK_LEFT, BACK_RIGHT -> BACK_HIP_OFFSET_X;
        };

        double z = switch (data.placement()) {
            case FRONT_LEFT, FRONT_RIGHT -> FRONT_HIP_OFFSET_Z;
            case SIDE_LEFT, SIDE_RIGHT -> SIDE_HIP_OFFSET_Z;
            case BACK_LEFT, BACK_RIGHT -> BACK_HIP_OFFSET_Z;
        };

        pose.translate(left ? offsetX : -offsetX, HIP_OFFSET_Y, z);
        pose.mulPose(Axis.YP.rotationDegrees(left ? -90f : 90f));
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
