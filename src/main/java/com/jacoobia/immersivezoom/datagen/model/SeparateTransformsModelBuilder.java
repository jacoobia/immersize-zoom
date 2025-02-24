package com.jacoobia.immersivezoom.datagen.model;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class SeparateTransformsModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

    private static final ResourceLocation LOADER =
            ResourceLocation.fromNamespaceAndPath("neoforge", "separate_transforms");

    public static final String ITEM_MODEL_SUFFIX = "_item";
    private static final String HANDHELD_MODEL_SUFFIX = "_handheld";

    private final String baseModel;
    private final String itemName;
    private final ModelProvider<?> modelProvider;

    public SeparateTransformsModelBuilder(T parent, ExistingFileHelper existingFileHelper, ModelProvider<?> modelProvider, String baseModel) {
        super(LOADER, parent, existingFileHelper, false);
        this.baseModel = baseModel + HANDHELD_MODEL_SUFFIX;
        this.itemName = "item/" + baseModel + ITEM_MODEL_SUFFIX;
        this.modelProvider = modelProvider;
    }

    @Override
    public @NotNull JsonObject toJson(JsonObject json) {
        json.addProperty("loader", "neoforge:separate_transforms");

        JsonObject baseObject = new JsonObject();
        baseObject.addProperty("parent", modelProvider.modLoc("item/" + baseModel).toString());
        json.add("base", baseObject);

        JsonObject perspectives = new JsonObject();

        JsonObject gui = new JsonObject();
        gui.addProperty("parent", modelProvider.modLoc(itemName).toString());
        perspectives.add("gui", gui);

        JsonObject ground = new JsonObject();
        ground.addProperty("parent", modelProvider.modLoc(itemName).toString());
        perspectives.add("ground", ground);

        JsonObject fixed = new JsonObject();
        fixed.addProperty("parent", modelProvider.modLoc(itemName).toString());
        perspectives.add("fixed", fixed);

        json.add("perspectives", perspectives);

        return json;
    }
}