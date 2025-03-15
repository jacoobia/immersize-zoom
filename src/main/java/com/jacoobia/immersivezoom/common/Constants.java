package com.jacoobia.immersivezoom.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Constants {

    public static final String MOD_ID = "immersive_zoom";

    public static final float MIN_ZOOM = 1.0f;
    public static final float MAX_ZOOM = 8.3f;
    public static final float DEFAULT_ZOOM = 1.5f;
    public static final float ZOOM_STEP = 0.1f;
    public static final float QUICK_ZOOM_STEP = 0.2f;

    public static final float MAX_FOV = 170.0f;
    public static final float MIN_FOV = 1.0f;

    public static final float ZOOMED_TRANSITION_MODIFIER = 0.5f;

    public static final int SPYGLASS_SLOT_INDEX = 99;

    public static final int DEFAULT_ZOOM_KEY = InputConstants.KEY_Z;

    public static final ResourceLocation CUSTOM_INVENTORY_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "textures/gui/container/iz_inventory.png");

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public static final Item[] ZOOM_ITEMS = new Item[] {
            ItemRegistry.ATTUNED_SPYGLASS.get(),
            Items.SPYGLASS
    };

}
