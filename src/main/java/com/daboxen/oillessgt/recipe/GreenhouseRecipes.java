package com.daboxen.oillessgt.recipe;

import com.daboxen.oillessgt.OillessGTMod;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.daboxen.oillessgt.recipe.OillessRecipeTypes.*;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static net.minecraft.world.item.Items.*;

/**
 * Much of this code is stolen from {@link com.gregtechceu.gtceu.data.recipe.misc.WoodMachineRecipes}. Yoink; it's mine
 * now!
 */
public class GreenhouseRecipes {

    @Getter
    private static final List<GreenhouseWoodTypeEntry> TREE_TYPE_ENTRIES;
    static {
        TREE_TYPE_ENTRIES = Arrays.asList(
                new GreenhouseWoodTypeEntry.Builder("minecraft", "oak")
                        .sapling(OAK_SAPLING)
                        .log(OAK_LOG)
                        .bonusItem(new ItemStack(APPLE, 4))
                        .build(),
                new GreenhouseWoodTypeEntry.Builder("minecraft", "azalea")
                        .sapling(AZALEA)
                        .log(OAK_LOG)
                        .build(),
                new GreenhouseWoodTypeEntry.Builder("minecraft", "flowering_azalea")
                        .sapling(FLOWERING_AZALEA)
                        .log(OAK_LOG)
                        .build(),
                new GreenhouseWoodTypeEntry.Builder("minecraft", "spruce")
                        .sapling(SPRUCE_SAPLING)
                        .log(SPRUCE_LOG)
                        .build(),
                new GreenhouseWoodTypeEntry.Builder("minecraft", "birch")
                        .sapling(BIRCH_SAPLING)
                        .log(BIRCH_LOG)
                        .build(),
                new GreenhouseWoodTypeEntry.Builder("minecraft", "jungle")
                        .sapling(JUNGLE_SAPLING)
                        .log(JUNGLE_LOG)
                        .build(),
                new GreenhouseWoodTypeEntry.Builder("minecraft", "acacia")
                        .sapling(ACACIA_SAPLING)
                        .log(ACACIA_LOG)
                        .build(),
                new GreenhouseWoodTypeEntry.Builder("minecraft", "dark_oak")
                        .sapling(DARK_OAK_SAPLING)
                        .log(DARK_OAK_LOG)
                        .bonusItem(new ItemStack(APPLE, 4))
                        .build(),
                new GreenhouseWoodTypeEntry.Builder("minecraft", "cherry")
                        .sapling(CHERRY_SAPLING)
                        .log(CHERRY_LOG)
                        .build(),
                new GreenhouseWoodTypeEntry.Builder("minecraft", "mangrove")
                        .sapling(MANGROVE_PROPAGULE)
                        .log(MANGROVE_LOG)
                        .bonusItem(new ItemStack(MANGROVE_ROOTS, 16))
                        .build(),
                new GreenhouseWoodTypeEntry.Builder("minecraft", "rubber")
                        .sapling(RUBBER_SAPLING.asItem())
                        .log(RUBBER_LOG.asItem())
                        .bonusItem(new ItemStack(STICKY_RESIN, 32))
                        .build());
    }

    private static final List<GreenhouseCropTypeEntry> CROP_TYPE_ENTRIES;
    static {
        CROP_TYPE_ENTRIES = Arrays.asList(
                new GreenhouseCropTypeEntry.Builder("minecraft", "wheat")
                        .seed(WHEAT_SEEDS)
                        .crop(WHEAT)
                        .build(),
                new GreenhouseCropTypeEntry.Builder("minecraft", "carrot")
                        .seed(CARROT)
                        .build(),
                new GreenhouseCropTypeEntry.Builder("minecraft", "beetroot")
                        .seed(BEETROOT_SEEDS)
                        .crop(BEETROOT)
                        .build(),
                new GreenhouseCropTypeEntry.Builder("minecraft", "melon")
                        .seed(MELON_SEEDS)
                        .crop(MELON)
                        .build(),
                new GreenhouseCropTypeEntry.Builder("minecraft", "pumpkin")
                        .seed(PUMPKIN_SEEDS)
                        .crop(PUMPKIN)
                        .build(),
                new GreenhouseCropTypeEntry.Builder("minecraft", "pitcher")
                        .seed(PITCHER_POD)
                        .crop(PITCHER_PLANT)
                        .build(),
                new GreenhouseCropTypeEntry.Builder("minecraft", "torchflower")
                        .seed(TORCHFLOWER_SEEDS)
                        .crop(TORCHFLOWER)
                        .build());
    }

    /**
     * For addons! Register a new tree type entry for the greenhouse.
     * 
     * @param entry The entry to add.
     * @apiNote For addon use! This is unused in the base mod.
     */
    @SuppressWarnings("unused")
    public static void addTreeType(GreenhouseWoodTypeEntry entry) {
        TREE_TYPE_ENTRIES.add(entry);
    }

    /**
     * For addons! Register a new crop type entry for the greenhouse.
     * 
     * @param entry The entry to add.
     * @apiNote For addon use! This is unused in the base mod.
     */
    @SuppressWarnings("unused")
    public static void addCropType(GreenhouseCropTypeEntry entry) {
        CROP_TYPE_ENTRIES.add(entry);
    }

    public static void addRecipes(Consumer<FinishedRecipe> provider) {
        addTreeRecipes(provider);
        addCropRecipes(provider);
    }

    private static void addTreeRecipes(Consumer<FinishedRecipe> provider) {
        for (GreenhouseWoodTypeEntry entry : TREE_TYPE_ENTRIES) {
            GREENHOUSE_RECIPES.recipeBuilder(OillessGTMod.id("farm_%s".formatted(entry.woodName)))
                    .inputItems(entry.sapling, 8)
                    .circuitMeta(7)
                    .inputFluids(Water, 16000)
                    .duration(320).EUt(VA[LV])
                    .outputItems(entry.log, 64)
                    .outputItems(entry.sapling, 16)
                    .outputItems(entry.bonusItems.stream().map(s -> s.copyWithCount(s.getCount() * 2))
                            .toArray(ItemStack[]::new))
                    .addCondition(new SubstructureCondition("arboretum"))
                    .save(provider);
            GREENHOUSE_RECIPES.recipeBuilder(OillessGTMod.id("farm_%s_fertilizer".formatted(entry.woodName)))
                    .inputItems(entry.sapling, 8)
                    .inputItems(FERTILIZER, 32)
                    .circuitMeta(3)
                    .inputFluids(Water, 20000)
                    .duration(100).EUt(VA[MV])
                    .outputItems(entry.log, 96)
                    .outputItems(entry.sapling, 24)
                    .outputItems(entry.bonusItems.stream().map(s -> s.copyWithCount(s.getCount() * 3))
                            .toArray(ItemStack[]::new))
                    .addCondition(new SubstructureCondition("arboretum"))
                    .save(provider);
            GREENHOUSE_RECIPES.recipeBuilder(OillessGTMod.id("farm_%s_co2".formatted(entry.woodName)))
                    .inputItems(entry.sapling, 8)
                    .circuitMeta(5)
                    .inputFluids(Water, 16000)
                    .inputFluids(CarbonDioxide, 64000)
                    .duration(160).EUt(VA[LV])
                    .outputItems(entry.log, 128)
                    .outputItems(entry.sapling, 32)
                    .outputItems(entry.bonusItems.stream().map(s -> s.copyWithCount(s.getCount() * 4))
                            .toArray(ItemStack[]::new))
                    .addCondition(new SubstructureCondition("arboretum"))
                    .save(provider);
            GREENHOUSE_RECIPES.recipeBuilder(OillessGTMod.id("farm_%s_fertilizer_co2".formatted(entry.woodName)))
                    .inputItems(entry.sapling, 8)
                    .inputItems(FERTILIZER, 32)
                    .circuitMeta(1)
                    .inputFluids(Water, 20000)
                    .inputFluids(CarbonDioxide, 96000)
                    .duration(40).EUt(VA[MV])
                    .outputItems(entry.log, 192)
                    .outputItems(entry.sapling, 48)
                    .outputItems(entry.bonusItems.stream().map(s -> s.copyWithCount(s.getCount() * 6))
                            .toArray(ItemStack[]::new))
                    .addCondition(new SubstructureCondition("arboretum"))
                    .save(provider);
        }
    }

    public static void addCropRecipes(Consumer<FinishedRecipe> provider) {
        for (GreenhouseCropTypeEntry entry : CROP_TYPE_ENTRIES) {

        }
    }
}
