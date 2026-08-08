package com.daboxen.oillessgt.emi;

import com.gregtechceu.gtceu.integration.recipeviewer.emi.recipe.GTRecipeEMICategory;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;

import static com.daboxen.oillessgt.machines.OillessMachines.LARGE_PYROLYSER;
import static com.daboxen.oillessgt.recipe.OillessRecipeTypes.INERT_GAS_BOOST_RECIPES;

@EmiEntrypoint
@SuppressWarnings("unused") //The @EmiEntrypoint annotation ensures that this is in fact used. :)
public class OillessEMIPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.addWorkstation(GTRecipeEMICategory.machineCategory(INERT_GAS_BOOST_RECIPES.getCategory()), EmiStack.of(LARGE_PYROLYSER.get()));
    }
}