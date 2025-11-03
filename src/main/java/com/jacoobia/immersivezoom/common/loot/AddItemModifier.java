package com.jacoobia.immersivezoom.common.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class AddItemModifier extends LootModifier {

    public static final MapCodec<AddItemModifier> CODEC =
            RecordCodecBuilder.mapCodec(inst ->
                    LootModifier.codecStart(inst).and(
                            BuiltInRegistries.ITEM.byNameCodec().listOf().fieldOf("items").forGetter(m -> Arrays.asList(m.items))
                    ).and(Codec.BOOL.fieldOf("replace").forGetter(m -> m.replace)
                    ).apply(inst, (conditions, list, replace) ->
                            new AddItemModifier(conditions, list.toArray(Item[]::new), replace))
            );

    private final Item[] items;
    private final boolean replace;

    public AddItemModifier(LootItemCondition[] conditionsIn, Item[] items, boolean replace) {
        super(conditionsIn);
        this.items = items;
        this.replace = replace;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> loot,
                                                          @NotNull LootContext lootContext) {
        if (replace) {
            loot.clear();
        }

        for (Item item : items) {
            loot.add(new ItemStack(item));
        }

        return loot;
    }

    @Override
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

}
