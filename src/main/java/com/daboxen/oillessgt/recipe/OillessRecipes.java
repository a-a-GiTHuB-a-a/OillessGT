package com.daboxen.oillessgt.recipe;

import com.daboxen.oillessgt.api.ModifierHelper;
import com.daboxen.oillessgt.machines.multiblock.LargePyrolyserMachine;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

import static com.daboxen.oillessgt.additions.ProgressionPatches.Acetaldehyde;
import static com.daboxen.oillessgt.recipe.OillessRecipeTypes.*;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

public class OillessRecipes {
    public static void addInertGasBoostRecipes(Consumer<FinishedRecipe> provider) {
        for (Int2ObjectMap.Entry<Pair<FluidIngredient, ModifierFunction>> entry : LargePyrolyserMachine.inertGases.int2ObjectEntrySet()) {
            int tier = entry.getIntKey();
            Pair<FluidIngredient, ModifierFunction> pair = entry.getValue();
            FluidIngredient fluidPerTick = pair.getFirst();
            ModifierFunction effect = pair.getSecond();
            INERT_GAS_BOOST_RECIPES.recipeBuilder("boost_tier_%d".formatted(tier))
                    .perTick(true)
                    .inputFluids(fluidPerTick)
                    .hideDuration(true)
                    .addData("gas_tier", tier)
                    .addData("eut_multiplier", String.valueOf(ModifierHelper.getEutMultiplier(effect)))
                    .addData("duration_multiplier", String.valueOf(ModifierHelper.getDurationMultiplier(effect)))
                    .save(provider);
        }
    }

    public static void addLargePyrolyserRecipes(Consumer<FinishedRecipe> provider) {
        LARGE_PYROLYSE_RECIPES.recipeBuilder("activated_carbon_from_carbon").circuitMeta(1)
                .inputItems(dust, Carbon)
                .addData("minimum_gas_tier", 1)
                .outputItems(dust, ActivatedCarbon)
                .duration(640).EUt(40)
                .save(provider);

        LARGE_PYROLYSE_RECIPES.recipeBuilder("activated_carbon_from_charcoal").circuitMeta(1)
                .inputItems(dust, Charcoal)
                .addData("minimum_gas_tier", 1)
                .outputItems(dust, ActivatedCarbon)
                .duration(1280).EUt(40)
                .save(provider);

        // Charcoal Byproducts
        LARGE_PYROLYSE_RECIPES.recipeBuilder("log_to_charcoal_byproducts").circuitMeta(4)
                .inputItems(ItemTags.LOGS_THAT_BURN, 16)
                .addData("minimum_gas_tier", 1)
                .outputItems(Items.CHARCOAL, 20)
                .outputFluids(CharcoalByproducts.getFluid(4000))
                .duration(640).EUt(64)
                .save(provider);

        // Wood Tar
        LARGE_PYROLYSE_RECIPES.recipeBuilder("log_to_wood_tar").circuitMeta(9)
                .inputItems(ItemTags.LOGS_THAT_BURN, 16)
                .outputItems(Items.CHARCOAL, 20)
                .outputFluids(WoodTar.getFluid(1500))
                .duration(640).EUt(64)
                .save(provider);

        // Wood Gas
        LARGE_PYROLYSE_RECIPES.recipeBuilder("log_to_wood_gas").circuitMeta(5)
                .inputItems(ItemTags.LOGS_THAT_BURN, 16)
                .outputItems(Items.CHARCOAL, 20)
                .outputFluids(WoodGas.getFluid(1500))
                .duration(640).EUt(64)
                .save(provider);

        // Wood Vinegar
        LARGE_PYROLYSE_RECIPES.recipeBuilder("log_to_wood_vinegar").circuitMeta(7)
                .inputItems(ItemTags.LOGS_THAT_BURN, 16)
                .outputItems(Items.CHARCOAL, 20)
                .outputFluids(WoodVinegar.getFluid(3000))
                .duration(640).EUt(64)
                .save(provider);

        // Creosote
        LARGE_PYROLYSE_RECIPES.recipeBuilder("log_to_creosote").circuitMeta(1)
                .inputItems(ItemTags.LOGS_THAT_BURN, 16)
                .outputItems(Items.CHARCOAL, 20)
                .outputFluids(Creosote.getFluid(4000))
                .duration(640).EUt(64)
                .save(provider);

        // Creosote
        LARGE_PYROLYSE_RECIPES.recipeBuilder("coal_to_coke_creosote").circuitMeta(1)
                .inputItems(gem, Coal, 16)
                .outputItems(gem, Coke, 16)
                .outputFluids(Creosote.getFluid(8000))
                .duration(640).EUt(64)
                .save(provider);

        LARGE_PYROLYSE_RECIPES.recipeBuilder("coal_block_to_coke_creosote").circuitMeta(1)
                .inputItems(block, Coal, 8)
                .outputItems(block, Coke, 8)
                .outputFluids(Creosote.getFluid(32000))
                .duration(2560).EUt(64)
                .save(provider);

        // Biomass
        LARGE_PYROLYSE_RECIPES.recipeBuilder("bio_chaff_to_fermented_biomass").EUt(10).duration(200)
                .inputItems(BIO_CHAFF)
                .circuitMeta(2)
                .inputFluids(Water.getFluid(1500))
                .outputFluids(FermentedBiomass.getFluid(1500))
                .save(provider);

        LARGE_PYROLYSE_RECIPES.recipeBuilder("bio_chaff_to_biomass").EUt(10).duration(900)
                .inputItems(BIO_CHAFF, 4)
                .circuitMeta(1)
                .inputFluids(Water.getFluid(4000))
                .outputFluids(Biomass.getFluid(5000))
                .save(provider);

        // Sugar to Charcoal
        LARGE_PYROLYSE_RECIPES.recipeBuilder("sugar_to_charcoal").circuitMeta(1)
                .inputItems(dust, Sugar, 23)
                .outputItems(dust, Charcoal, 12)
                .outputFluids(Water.getFluid(1500))
                .duration(320).EUt(64)
                .save(provider);

        // COAL GAS ============================================

        // From Log
        LARGE_PYROLYSE_RECIPES.recipeBuilder("log_to_coal_gas").circuitMeta(20)
                .inputItems(ItemTags.LOGS_THAT_BURN, 16)
                .inputFluids(Steam.getFluid(1000))
                .outputItems(Items.CHARCOAL, 20)
                .outputFluids(CoalGas.getFluid(2000))
                .duration(640).EUt(64)
                .save(provider);

        // From Coal
        LARGE_PYROLYSE_RECIPES.recipeBuilder("coal_to_coal_gas").circuitMeta(22)
                .inputItems(gem, Coal, 16)
                .inputFluids(Steam.getFluid(1000))
                .outputItems(gem, Coke, 16)
                .outputFluids(CoalGas.getFluid(4000))
                .duration(320).EUt(96)
                .save(provider);

        LARGE_PYROLYSE_RECIPES.recipeBuilder("coal_block_to_coal_gas").circuitMeta(22)
                .inputItems(block, Coal, 8)
                .inputFluids(Steam.getFluid(4000))
                .outputItems(block, Coke, 8)
                .outputFluids(CoalGas.getFluid(16000))
                .duration(1280).EUt(96)
                .save(provider);

        // COAL TAR ============================================
        LARGE_PYROLYSE_RECIPES.recipeBuilder("charcoal_to_coal_tar").circuitMeta(8)
                .inputItems(Items.CHARCOAL, 32)
                .chancedOutput(dust, Ash, 5000)
                .outputFluids(CoalTar.getFluid(1000))
                .duration(640).EUt(64)
                .save(provider);

        LARGE_PYROLYSE_RECIPES.recipeBuilder("coal_to_coal_tar").circuitMeta(8)
                .inputItems(Items.COAL, 12)
                .chancedOutput(dust, DarkAsh, 5000)
                .outputFluids(CoalTar.getFluid(3000))
                .duration(320).EUt(96)
                .save(provider);

        LARGE_PYROLYSE_RECIPES.recipeBuilder("coke_to_coal_tar").circuitMeta(8)
                .inputItems(gem, Coke, 8)
                .chancedOutput(dust, Ash, 7500)
                .outputFluids(CoalTar.getFluid(4000))
                .duration(320).EUt(96)
                .save(provider);
    }

    public static void addButadieneSynthesisRecipes(Consumer<FinishedRecipe> provider) {
        ELECTROLYZER_RECIPES.recipeBuilder("ethanol_electrolysis")
                .inputFluids(Ethanol, 1000)
                .outputFluids(Acetaldehyde.getFluid(1000))
                .outputFluids(Hydrogen.getFluid(2000))
                .duration(600).EUt(VA[MV]).save(provider);
        CHEMICAL_RECIPES.recipeBuilder("butadiene_synthesis")
                .notConsumable(dust, Tantalum, 1)
                .inputFluids(Ethanol, 1000)
                .inputFluids(Acetaldehyde, 1000)
                .outputFluids(Butadiene.getFluid(1000))
                .outputFluids(Water.getFluid(2000))
                .duration(200).EUt(VA[HV]).save(provider);
    }
}
