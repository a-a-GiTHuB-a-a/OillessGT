package com.daboxen.oillessgt.additions;

import com.daboxen.oillessgt.OillessGTMod;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import net.minecraft.data.recipes.FinishedRecipe;
import java.util.function.Consumer;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.DISABLE_DECOMPOSITION;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

public class ProgressionPatches {
    public static Material Acetaldehyde;

    public static void registerMaterials() {
        Acetaldehyde = new Material.Builder(OillessGTMod.id("acetaldehyde"))
                .gas()
                .color(0xbf7f5f)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 2, Hydrogen, 4, Oxygen, 1)
                .buildAndRegister();
    }

    public static void addRecipes(Consumer<FinishedRecipe> provider) {
        CHEMICAL_RECIPES.recipeBuilder("acetaldehyde_synthesis")
                .notConsumable(dust, Copper, 1)
                .inputFluids(Ethanol, 1000)
                .outputFluids(Acetaldehyde.getFluid(1000))
                .outputFluids(Hydrogen.getFluid(2000))
                .duration(100).EUt(VA[EV]).save(provider);
        CHEMICAL_RECIPES.recipeBuilder("butadiene_synthesis")
                .notConsumable(dust, Tantalum, 1)
                .inputFluids(Ethanol, 1000)
                .inputFluids(Acetaldehyde, 1000)
                .outputFluids(Butadiene.getFluid(1000))
                .outputFluids(Water.getFluid(2000))
                .duration(200).EUt(VA[HV]).save(provider);
    }
}