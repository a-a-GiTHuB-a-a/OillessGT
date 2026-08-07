package com.daboxen.oillessgt.recipe;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.daboxen.oillessgt.additions.ProgressionPatches.Acetaldehyde;
import static com.daboxen.oillessgt.recipe.OillessRecipeTypes.*;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

public class OillessRecipes {
    public static void addLargePyrolyserRecipes(Consumer<FinishedRecipe> provider) {
        LARGE_PYROLYSE_RECIPES.recipeBuilder("activated_carbon_from_carbon").circuitMeta(1)
                .inputItems(dust, Carbon)
                .addData("minimum_gas_tier", 1)
                .outputItems(dust, ActivatedCarbon)
                .duration(320).EUt(64)
                .save(provider);

        LARGE_PYROLYSE_RECIPES.recipeBuilder("activated_carbon_from_charcoal").circuitMeta(1)
                .inputItems(dust, Charcoal)
                .addData("minimum_gas_tier", 1)
                .outputItems(dust, ActivatedCarbon)
                .duration(640).EUt(64)
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
