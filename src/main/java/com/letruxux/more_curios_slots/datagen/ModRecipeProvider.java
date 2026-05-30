package com.letruxux.more_curios_slots.datagen;

import com.letruxux.more_curios_slots.MoreCuriosSlots;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    private static final ICondition CURIOS_LOADED = new ModLoadedCondition("curios");

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        RecipeOutput conditionalOutput = consumer.withConditions(CURIOS_LOADED);

        for (int i = 0; i < MoreCuriosSlots.ALL_CURIO_NAMES.size(); i++) {
            String slotName = MoreCuriosSlots.ALL_CURIO_NAMES.get(i);
            TagKey<Item> slotTag = TagKey.create(Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath("curios", slotName));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MoreCuriosSlots.EXTRA_SLOT_ITEMS.get(i).get())
                    .pattern("NEN")
                    .pattern("DSD")
                    .pattern("NGN")
                    .define('N', Items.IRON_BLOCK)
                    .define('D', Items.DIAMOND_BLOCK)
                    .define('E', Items.EMERALD_BLOCK)
                    .define('G', Items.GOLD_BLOCK)
                    .define('S', slotTag)
                    .unlockedBy("has_" + slotName, has(slotTag))
                    .save(conditionalOutput);
        }
    }
}
