package com.daboxen.oillessgt.api;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

public class ModifierHelper {
    /**
     * ONLY USE THIS FOR TESTING {@link ModifierFunction ModifierFunctions}!
     * It seems like {@link GTValues#M} can be an {@code int}, so the cast should work.
     */
    private static final GTRecipe MODIFIER_DUMMY = GTRecipeBuilder.ofRaw().EUt(GTValues.M).duration((int)GTValues.M).buildRawRecipe();

    /**
     * This function will not work for a modifier that does not purely multiply.
     * @param function A function that applies a MULTIPLIER to the EU/t of a {@link com.gregtechceu.gtceu.api.recipe.GTRecipe GTRecipe}.
     * @return The multiplier that is applied to EU/t.
     */
    public static double getEutMultiplier(ModifierFunction function) {
        GTRecipe modifiedDummy = function.apply(MODIFIER_DUMMY);

        assert modifiedDummy != null;
        return (double)modifiedDummy.getInputEUt().getTotalEU()/MODIFIER_DUMMY.getInputEUt().getTotalEU();
    }
    /**
     * This function will not work for a modifier that does not purely multiply.
     * @param function A function that applies a MULTIPLIER to the duration of a {@link com.gregtechceu.gtceu.api.recipe.GTRecipe GTRecipe}.
     * @return The multiplier that is applied to duration.
     */
    public static double getDurationMultiplier(ModifierFunction function) {
        GTRecipe modifiedDummy = function.apply(MODIFIER_DUMMY);

        assert modifiedDummy != null;
        return (double)modifiedDummy.duration/MODIFIER_DUMMY.duration;
    }
}