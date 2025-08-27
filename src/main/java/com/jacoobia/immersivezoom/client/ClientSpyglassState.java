package com.jacoobia.immersivezoom.client;

import com.jacoobia.immersivezoom.common.item.AttunedSpyglass;
import com.jacoobia.immersivezoom.common.utils.ClientUtils;
import com.jacoobia.immersivezoom.common.utils.SpyglassUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static com.jacoobia.immersivezoom.common.Constants.*;

@OnlyIn(Dist.CLIENT)
public class ClientSpyglassState {

    private static boolean serverScoping;
    private static boolean localHeld;
    private static float localZoom = ATTUNED_ZOOM_DEFAULT;

    public static void setScoping(boolean scoping) {
        serverScoping = scoping;
    }

    public static float getLocalZoom() {
        return localZoom;
    }

    public static void setLocalZoom(float zoom) {
        localZoom = Math.max(ATTUNED_ZOOM_MIN, Math.min(ATTUNED_ZOOM_MAX, zoom));
    }

    public static boolean isServerScoping() {
        return serverScoping;
    }

    public static boolean isScopingInHand() {
        if (ClientUtils.isThirdPersonFront()) return false;
        LocalPlayer player = ClientUtils.getLocalPlayerCast();
        return player != null
                && player.isUsingItem()
                && SpyglassUtils.isSpyglass(player.getUseItem());
    }

    public static boolean isScopingWithHotkey() {
        if (ClientUtils.isThirdPersonFront()) return false;
        if (!isLocalHeld()) return false;
        LocalPlayer player = ClientUtils.getLocalPlayerCast();
        if (player == null) return false;
        ItemStack slotStack = player.getInventory().getItem(SPYGLASS_SLOT_INDEX);
        return SpyglassUtils.isSpyglass(slotStack);
    }

    public static boolean isScopingAny() {
        if (ClientUtils.isThirdPersonFront()) return false;
        if (isScopingInHand() || isScopingWithHotkey()) return true;
        if (!isServerScoping()) return false;
        return !getActiveSpyglassClient().isEmpty();
    }

    public static ItemStack getActiveSpyglassClient() {
        LocalPlayer player = ClientUtils.getLocalPlayerCast();
        if (player == null) return ItemStack.EMPTY;

        if (!ClientUtils.isThirdPersonFront() && (isLocalHeld() || isServerScoping())) {
            ItemStack slot = player.getInventory().getItem(SPYGLASS_SLOT_INDEX);
            if (SpyglassUtils.isSpyglass(slot)) return slot;
        }
        ItemStack main = player.getMainHandItem();
        if (SpyglassUtils.isSpyglass(main)) return main;
        ItemStack off = player.getOffhandItem();
        if (SpyglassUtils.isSpyglass(off)) return off;
        return ItemStack.EMPTY;
    }

    public static boolean isAttunedActive() {
        ItemStack s = getActiveSpyglassClient();
        return !s.isEmpty() && s.getItem() instanceof AttunedSpyglass;
    }

    public static void seedLocalZoom() {
        ItemStack activeSpyglassStack = getActiveSpyglassClient();
        if (!activeSpyglassStack.isEmpty() && activeSpyglassStack.getItem() instanceof AttunedSpyglass) {
            float current = AttunedSpyglass.getZoom(activeSpyglassStack);
            setLocalZoom(Mth.clamp(current, ATTUNED_ZOOM_MIN, ATTUNED_ZOOM_MAX));
        } else {
            setLocalZoom(VANILLA_ZOOM);
        }
    }

    public static boolean isLocalHeld() {
        return localHeld;
    }

    public static void setLocalHeld(boolean localHeld) {
        ClientSpyglassState.localHeld = localHeld;
    }

}
