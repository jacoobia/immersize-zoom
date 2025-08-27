package com.jacoobia.immersivezoom.client.event;

import com.jacoobia.immersivezoom.client.ClientSpyglassState;
import com.jacoobia.immersivezoom.common.item.AttunedSpyglass;
import com.jacoobia.immersivezoom.common.network.packet.SpyglassScopePacket;
import com.jacoobia.immersivezoom.common.utils.ClientUtils;
import com.jacoobia.immersivezoom.common.utils.SpyglassUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.jacoobia.immersivezoom.common.Constants.*;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientInputSubscriber {

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        LocalPlayer localPlayer = ClientUtils.getLocalPlayerCast();
        if (localPlayer == null) return;

        if (ClientUtils.isThirdPersonFront()) return;

        ItemStack activeSpyglassStack = ClientSpyglassState.getActiveSpyglassClient();
        boolean isVanillaInHandScoping =
                localPlayer.isUsingItem() && SpyglassUtils.isSpyglass(localPlayer.getUseItem());
        boolean isHotkeyScoping =
                ClientSpyglassState.isLocalHeld() && SpyglassUtils.isSpyglass(activeSpyglassStack);

        if (!(isVanillaInHandScoping || isHotkeyScoping)) return;

        event.setCanceled(true);

        if (!(activeSpyglassStack.getItem() instanceof AttunedSpyglass)) return;

        double mouseWheelDelta = event.getScrollDeltaY();
        if (mouseWheelDelta == 0) return;

        boolean isShiftHeld = localPlayer.isShiftKeyDown();
        float stepPerNotch = isShiftHeld ? QUICK_ZOOM_STEP : ZOOM_STEP;
        float signedStep   = (float)Math.signum(mouseWheelDelta) * stepPerNotch;

        float currentZoom = AttunedSpyglass.getZoom(activeSpyglassStack);
        float nextZoom    = Mth.clamp(currentZoom + signedStep, ATTUNED_ZOOM_MIN, ATTUNED_ZOOM_MAX);

        // Client prediction for smooth feel
        AttunedSpyglass.setZoom(activeSpyglassStack, nextZoom);
        ClientSpyglassState.setLocalZoom(nextZoom);

        PacketDistributor.sendToServer(SpyglassScopePacket.setFinal(nextZoom));
    }

}

