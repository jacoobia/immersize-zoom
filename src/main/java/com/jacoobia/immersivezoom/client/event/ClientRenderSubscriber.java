package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.client.utils.ClientUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientRenderSubscriber {

    // Rendering is single-threaded and Pre/Post always pair up, so one saved pose is enough
    private static HumanoidModel.ArmPose savedRightArmPose = null;

    @SubscribeEvent
    public static void onPlayerRenderPre(RenderPlayerEvent.Pre event) {
        if (shouldNotPlayAnimation(event.getEntity())) return;

        var model = (HumanoidModel<?>) event.getRenderer().getModel();
        savedRightArmPose = model.rightArmPose;
        model.rightArmPose = HumanoidModel.ArmPose.SPYGLASS;
    }

    @SubscribeEvent
    public static void onPlayerRenderPost(RenderPlayerEvent.Post event) {
        if (savedRightArmPose == null) return;

        var model = (HumanoidModel<?>) event.getRenderer().getModel();
        model.rightArmPose = savedRightArmPose;
        savedRightArmPose = null;
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
