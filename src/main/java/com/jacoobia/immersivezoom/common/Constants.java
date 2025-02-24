package com.jacoobia.immersivezoom.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Constants {

    public static final String MOD_ID = "immersive_zoom";

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public static final Item[] SCOPE_ITEMS = new Item[] {
            ItemRegistry.ATTUNED_SPYGLASS.get(),
            Items.SPYGLASS
    };

}
