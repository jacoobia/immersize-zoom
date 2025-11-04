package com.jacoobia.immersivezoom.client.render;

import com.jacoobia.immersivezoom.client.config.ClientConfig;
import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import com.jacoobia.immersivezoom.common.registry.AttachmentRegistry;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import com.jacoobia.immersivezoom.common.utils.BeltPlacement;
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
    private static final double FRONT_HIP_OFFSET_X = 0.2;
    private static final double FRONT_HIP_OFFSET_Z = -0.2;
    private static final double SIDE_HIP_OFFSET_X = 0.25;
    private static final double SIDE_HIP_OFFSET_Z = 0.0;

    private static final float WALK_SWING = 3f;
    private static final float WALK_SWING_RATE = 6f;
    private static final float WALKING_BOB = 0.1f;
    private static final float HANG = 0.2f;

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
        pose.translate(offsetX(data.placement()), HIP_OFFSET_Y, offsetZ(data.placement()));

        if (ClientConfig.BELT_SWAY.get()) {
            pose.translate(0, -HANG, 0);
            pose.mulPose(Axis.XP.rotationDegrees(BeltPhysics.pitch(player, partialTicks) + walkSwing(player, partialTicks)));
            pose.mulPose(Axis.ZP.rotationDegrees(BeltPhysics.roll(player, partialTicks)));
            pose.translate(0, HANG, 0);
        }

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

    /**
     * @param placement the belt placement
     * @return the spyglass's X offset from the body pivot, +X is the player's left
     */
    static double offsetX(BeltPlacement placement) {
        double offset = switch (placement) {
            case FRONT_LEFT, FRONT_RIGHT -> FRONT_HIP_OFFSET_X;
            case SIDE_LEFT, SIDE_RIGHT -> SIDE_HIP_OFFSET_X;
            case BACK_LEFT, BACK_RIGHT -> BACK_HIP_OFFSET_X;
        };
        return placement.isLeft() ? offset : -offset;
    }

    /**
     * @param placement the belt placement
     * @return the spyglass's Z offset from the body pivot, +Z is the player's back
     */
    static double offsetZ(BeltPlacement placement) {
        return switch (placement) {
            case FRONT_LEFT, FRONT_RIGHT -> FRONT_HIP_OFFSET_Z;
            case SIDE_LEFT, SIDE_RIGHT -> SIDE_HIP_OFFSET_Z;
            case BACK_LEFT, BACK_RIGHT -> BACK_HIP_OFFSET_Z;
        };
    }

    /**
     * A small swing with each step, zero when standing still or in the air
     * @param player the player wearing the spyglass
     * @param partialTicks the render partial tick
     * @return the swing in degrees
     */
    private static float walkSwing(AbstractClientPlayer player, float partialTicks) {
        float bob = Mth.lerp(partialTicks, player.oBob, player.bob);
        float walked = Mth.lerp(partialTicks, player.walkDistO, player.walkDist);
        return Mth.sin(walked * WALK_SWING_RATE) * WALK_SWING * (bob / WALKING_BOB);
    }
}
