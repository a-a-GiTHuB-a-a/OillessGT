package com.daboxen.oillessgt.machines.multiblock;

import com.daboxen.oillessgt.OillessGTMod;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.integration.recipeviewer.emi.recipe.GTEmiRecipe;
import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import static com.daboxen.oillessgt.machines.OillessMachines.LARGE_PYROLYSER;

public class InertGasBoostEMICategory extends EmiRecipeCategory {
    public static final InertGasBoostEMICategory CATEGORY = new InertGasBoostEMICategory();

    public InertGasBoostEMICategory() {
        super(OillessGTMod.id("inert_gas_boost"), EmiStack.of(LARGE_PYROLYSER.asStack()));
    }

    public static void registerDisplays(EmiRegistry registry) {
        for (Int2ObjectMap.Entry<Pair<FluidIngredient, ModifierFunction>> entry : LargePyrolyserMachine.inertGases.int2ObjectEntrySet()) {
            Pair<FluidIngredient, ModifierFunction> pair = entry.getValue();
            FluidIngredient gasPerTick = pair.getFirst();
            ModifierFunction recipeModifier = pair.getSecond();

            GTRecipeBuilder pseudoRecipe = GTRecipeBuilder.ofRaw();
            pseudoRecipe.perTick(true);
            pseudoRecipe.inputFluids(gasPerTick);

            GTEmiRecipe visualRecipe = new GTEmiRecipe(pseudoRecipe.buildRawRecipe(), CATEGORY);

            registry.addRecipe(visualRecipe);
        }
    }

    public static void registerWorkstations(EmiRegistry registry) {
        registry.addWorkstation(CATEGORY, EmiStack.of(LARGE_PYROLYSER.asStack()));
    }

    public static void initLang(RegistrateLangProvider provider) {
        provider.add("emi.category.oillessgt.inert_gas_boost", "Inert Gas Boosting");
    }
}
