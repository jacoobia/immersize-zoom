package com.jacoobia.immersivezoom.common;

import com.jacoobia.immersivezoom.common.utils.BeltPlacement;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.resources.ResourceLocation;

public class Constants {

    public static final String MOD_ID = "immersive_zoom";
    public static final String MOD_NAME = "Immersive Zoom";

    public static final float ATTUNED_ZOOM_MIN = 3.0f;
    public static final float ATTUNED_ZOOM_MAX = 30.0f;
    public static final float ATTUNED_ZOOM_DEFAULT = 10.0f;

    public static final float VANILLA_ZOOM = 10.0f;

    // Zoom is multiplied/divided by these per scroll notch, so each notch feels the same at any zoom level
    public static final float ZOOM_STEP = 1.05f;
    public static final float QUICK_ZOOM_STEP = 1.25f;

    public static final float ZOOMED_TRANSITION_MODIFIER = 0.5f;

    public static final int SPYGLASS_SLOT_INDEX = 41;

    public static final int DEFAULT_ZOOM_KEY = InputConstants.KEY_Z;

    public static final BeltPlacement DEFAULT_BELT_PLACEMENT = BeltPlacement.BACK_RIGHT;
    public static final boolean DEFAULT_SHOW_BELT_SPYGLASS = true;
    public static final boolean DEFAULT_RENDER_BELT_SPYGLASSES = true;
    public static final boolean DEFAULT_BELT_SWAY = true;
    public static final boolean DEFAULT_ZOOM_SOUND = true;

    public static final int SPYGLASS_SLOT_X = 77;
    public static final int SPYGLASS_SLOT_Y = 44;
    public static final int CREATIVE_SPYGLASS_SLOT_X = 127;
    public static final int CREATIVE_SPYGLASS_SLOT_Y = 20;

    public static final ResourceLocation EMPTY_SLOT_SPYGLASS =
            ResourceLocation.fromNamespaceAndPath(MOD_ID,"item/empty_slot_spyglass");

}
