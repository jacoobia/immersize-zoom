package com.jacoobia.immersivezoom.datagen.providers;

import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.data.recipes.RecipeCategory.MISC;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(MISC, ItemRegistry.ATTUNED_SPYGLASS)
                .pattern(" A ")
                .pattern(" F ")
                .pattern(" S ")
                .define('A', ItemRegistry.ATTUNED_AMETHYST_SHARD)
                .define('F', ItemRegistry.COPPER_FOCUS)
                .define('S', Items.SPYGLASS)
                .unlockedBy("has_spyglass", has(Items.SPYGLASS))
                .unlockedBy("has_copper_focus", has(ItemRegistry.COPPER_FOCUS))
                .unlockedBy("has_attuned_shard", has(ItemRegistry.ATTUNED_AMETHYST_SHARD))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, ItemRegistry.COPPER_FOCUS)
                .pattern(" C ")
                .pattern("CRC")
                .pattern(" C ")
                .define('C', Items.COPPER_INGOT)
                .define('R', Items.COMPARATOR)
                .unlockedBy("has_copper", has(Items.COPPER_INGOT))
                .unlockedBy("has_comparator", has(Items.COMPARATOR))
                .save(recipeOutput);
    }

}
