package com.jacoobia.immersivezoom.common;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.resources.ResourceLocation;

public class Constants {

    public static final String MOD_ID = "immersive_zoom";
    public static final String MOD_NAME = "Immersive Zoom";

    public static final float ATTUNED_ZOOM_MIN = 3.0f;
    public static final float ATTUNED_ZOOM_MAX = 30.0f;
    public static final float ATTUNED_ZOOM_DEFAULT = 3.0f;

    public static final float VANILLA_ZOOM = 10.0f;

    public static final float ZOOM_STEP = 0.1f;
    public static final float QUICK_ZOOM_STEP = 0.5f;

    public static final float ZOOMED_TRANSITION_MODIFIER = 0.5f;

    public static final int SPYGLASS_SLOT_INDEX = 41;

    public static final int DEFAULT_ZOOM_KEY = InputConstants.KEY_Z;

    public static final ResourceLocation CUSTOM_INVENTORY_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/gui/container/iz_inventory.png");

    public static final ResourceLocation EMPTY_SLOT_SPYGLASS =
            ResourceLocation.fromNamespaceAndPath(MOD_ID,"item/empty_slot_spyglass");

}
