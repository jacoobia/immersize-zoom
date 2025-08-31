package com.jacoobia.immersivezoom.datagen.providers;

import com.jacoobia.immersivezoom.common.loot.AddItemModifier;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.concurrent.CompletableFuture;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, MOD_ID);
    }

    @Override
    protected void start() {
        this.add("unattuned_amethyst_shard_from_amethyst",
                new AddItemModifier(
                        new LootItemCondition[] {
                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.AMETHYST_CLUSTER).build(),
                                LootItemRandomChanceCondition.randomChance(0.33f).build()
                }, new Item[] { ItemRegistry.UNATTUNED_AMETHYST_SHARD.get() }, true));
    }

}
