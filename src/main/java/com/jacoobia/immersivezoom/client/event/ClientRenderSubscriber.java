package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import com.jacoobia.immersivezoom.mixin.accessors.ItemInHandAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;
import static com.jacoobia.immersivezoom.common.Constants.SPYGLASS_SLOT_INDEX;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientRenderSubscriber {

    @SubscribeEvent
    public static void onPlayerRenderPre(RenderPlayerEvent.Pre event) {
        if (shouldNotPlayAnimation(event.getEntity())) return;

        var model = (HumanoidModel<?>) event.getRenderer().getModel();
        event.getEntity().getPersistentData().putString("scope_pose_right",
                model.rightArmPose.name());
        event.getEntity().getPersistentData().putString("scope_pose_left",
                model.leftArmPose.name());

        model.rightArmPose = HumanoidModel.ArmPose.SPYGLASS;
    }

    @SubscribeEvent
    public static void onPlayerRenderPost(RenderPlayerEvent.Post event) {
        if (shouldNotPlayAnimation(event.getEntity())) return;

        var model = (HumanoidModel<?>) event.getRenderer().getModel();
        var tag = event.getEntity().getPersistentData();
        try {
            model.rightArmPose = HumanoidModel.ArmPose
                    .valueOf(tag.getString("scope_pose_right"));
            model.leftArmPose = HumanoidModel.ArmPose
                    .valueOf(tag.getString("scope_pose_left"));
        } catch (Exception ignored) {}
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (shouldNotPlayAnimation(ClientUtils.getLocalPlayerCast())) return;

        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        ItemStack spyglass = player.getInventory().getItem(SPYGLASS_SLOT_INDEX);
        if (spyglass.isEmpty()) return;

        event.setCanceled(true);

        ItemInHandRenderer inHand = Minecraft.getInstance().gameRenderer.itemInHandRenderer;

        var pose = event.getPoseStack();
        var buffers = event.getMultiBufferSource();
        int light = event.getPackedLight();

        float equip = event.getEquipProgress();
        float swing = 0.0F;

        ((ItemInHandAccessor) inHand).callRenderPlayerArm(
                pose, buffers, light, equip, swing, player.getMainArm()
        );

        pose.pushPose();
        pose.popPose();
    }

    /**
     * Conditions where we should NOT play the spyglass animation
     * @return true if we should NOT play the animation
     */
    private static boolean shouldNotPlayAnimation(Player player) {
        if (player == null) return true;
        if (player.isLocalPlayer() && !ClientUtils.isThirdPerson()) return true;
        return !ClientUtils.isHotkeyScoping(player);
    }

}
