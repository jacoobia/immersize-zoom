package com.jacoobia.immersivezoom.datagen.providers;

import com.jacoobia.immersivezoom.common.Translations;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

public class ModAdvancementProvider extends AdvancementProvider {

    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new Generator()));
    }

    private static class Generator implements AdvancementGenerator {

        private static final ResourceLocation BACKGROUND =
                ResourceLocation.withDefaultNamespace("textures/gui/advancements/backgrounds/adventure.png");

        @Override
        public void generate(HolderLookup.@NotNull Provider provider,
                             @NotNull Consumer<AdvancementHolder> consumer,
                             @NotNull ExistingFileHelper existingFileHelper) {

            var root = Advancement.Builder.advancement()
                    .display(new ItemStack(Items.SPYGLASS),
                            Translations.Advcancements.rootName,
                            Translations.Advcancements.rootDescription,
                            BACKGROUND,
                            AdvancementType.TASK, true, true, false)
                    .addCriterion("has_item",
                            InventoryChangeTrigger.TriggerInstance.hasItems(Items.SPYGLASS))
                    .save(consumer,
                            ResourceLocation.fromNamespaceAndPath(MOD_ID, "immersive_zoom/root"),
                            existingFileHelper);

            var amethyst = Advancement.Builder.advancement()
                    .parent(root)
                    .display(new ItemStack(ItemRegistry.UNATTUNED_AMETHYST_SHARD.get()),
                            Translations.Advcancements.unattunedAmethyst,
                            Translations.Advcancements.unattunedAmethystDescription,
                            null, AdvancementType.TASK,
                            true, true, false)
                    .addCriterion("has_item",
                            InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.UNATTUNED_AMETHYST_SHARD.get()))
                    .save(consumer,
                            ResourceLocation.fromNamespaceAndPath(MOD_ID, "immersive_zoom/unattuned_amethyst_shard"),
                            existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(amethyst)
                    .display(new ItemStack(ItemRegistry.ATTUNED_AMETHYST_SHARD.get()),
                            Translations.Advcancements.attunedAmethyst,
                            Translations.Advcancements.attunedAmethystDescription,
                            null, AdvancementType.TASK,
                            true, true, false)
                    .addCriterion("has_item",
                            InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.ATTUNED_AMETHYST_SHARD.get()))
                    .save(consumer,
                            ResourceLocation.fromNamespaceAndPath(MOD_ID, "immersive_zoom/attuned_amethyst_shard"),
                            existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(root)
                    .display(new ItemStack(ItemRegistry.COPPER_FOCUS.get()),
                            Translations.Advcancements.copperFocus,
                            Translations.Advcancements.copperFocusDescription,
                            null, AdvancementType.TASK,
                            true, true, false)
                    .addCriterion("has_item",
                            InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.COPPER_FOCUS.get()))
                    .save(consumer,
                            ResourceLocation.fromNamespaceAndPath(MOD_ID, "immersive_zoom/copper_focus"),
                            existingFileHelper);

            Advancement.Builder.advancement()
                    .parent(root)
                    .display(new ItemStack(ItemRegistry.ATTUNED_SPYGLASS.get()),
                            Translations.Advcancements.attunedSpyglassName,
                            Translations.Advcancements.attunedSpyglassDescription,
                            null, AdvancementType.GOAL,
                            true, true, false)
                    .addCriterion("has_item",
                            InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.ATTUNED_SPYGLASS.get()))
                    .save(consumer,
                            ResourceLocation.fromNamespaceAndPath(MOD_ID, "immersive_zoom/attuned_spyglass"),
                            existingFileHelper);
        }

    }

}
