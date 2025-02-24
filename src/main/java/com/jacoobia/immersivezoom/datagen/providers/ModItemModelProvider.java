package com.jacoobia.immersivezoom.datagen.providers;

import com.jacoobia.immersivezoom.common.Constants;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import com.jacoobia.immersivezoom.datagen.model.SeparateTransformsModelBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import static com.jacoobia.immersivezoom.datagen.model.SeparateTransformsModelBuilder.ITEM_MODEL_SUFFIX;

@SuppressWarnings("SameParameterValue")
public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ItemRegistry.ATTUNED_AMETHYST_SHARD.get());
        handHeldModelItem(ItemRegistry.ATTUNED_SPYGLASS);
    }

    private void handHeldModelItem(@NotNull DeferredItem<Item> registryItem) {
        final String baseName = registryItem.getId().getPath();
        final String itemName = baseName + ITEM_MODEL_SUFFIX;

        getBuilder(baseName)
                .parent(getExistingFile(mcLoc("item/generated")))
                .customLoader((parent, existingFileHelper) ->
                        new SeparateTransformsModelBuilder<>(
                                getBuilder(baseName),
                                existingFileHelper,
                                this,
                                baseName))
                .end();

        getBuilder(itemName)
                .parent(getExistingFile(mcLoc("item/handheld")))
                .texture("layer0", modLoc("item/" + registryItem.getId().getPath()));
    }

}
