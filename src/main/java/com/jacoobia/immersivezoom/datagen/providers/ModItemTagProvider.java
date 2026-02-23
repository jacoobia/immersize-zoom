package com.jacoobia.immersivezoom.datagen.providers;

import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import com.jacoobia.immersivezoom.common.utils.SpyglassUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;
import static com.jacoobia.immersivezoom.common.Constants.ZOOM_ITEMS;

/**
 * Moved to using tags so I don't have to keep checking if the itemstack is
 * one of N itemstacks, this way if I want to I can add some future new spyglasses
 * like themed ones or something IDK but now {@link SpyglassUtils#isSpyglass} is just a tag check!
 */
public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CompletableFuture.completedFuture(TagsProvider.TagLookup.<Block>empty()), MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        tag(ZOOM_ITEMS).add(Items.SPYGLASS, ItemRegistry.ATTUNED_SPYGLASS.get());
    }

}
