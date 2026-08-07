package com.daboxen.oillessgt.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;
import com.gregtechceu.gtceu.common.mui.GTGuiTextures;

import static com.daboxen.oillessgt.OillessGTMod.OILLESS_LOGGER;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.MULTIBLOCK;

public class OillessRecipeTypes {
    public final static GTRecipeType LARGE_PYROLYSE_RECIPES = GTRecipeTypes.register("large_pyrolyser", MULTIBLOCK).setMaxIOSize(2, 1, 1, 1)
            .setEUIO(IO.IN)
            .UI(builder -> builder.setProgressBar(GTGuiTextures.PROGRESS_ARROW)
                    .addRecipeUIModifier(OillessUIModifiers.INERT_GAS_INFO))
            .setSound(GTSoundEntries.FIRE);

    public static void init() {
        OILLESS_LOGGER.info("Registering recipe types…");
    }
}