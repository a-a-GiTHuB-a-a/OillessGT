package com.daboxen.oillessgt.additions;

import com.daboxen.oillessgt.OillessGTMod;
import com.daboxen.oillessgt.api.RecipeBuilderHelper;
import com.daboxen.oillessgt.config.OillessConfiguration;
import com.daboxen.oillessgt.recipe.OillessRecipes;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import com.tterrag.registrate.providers.RegistrateLangProvider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static com.daboxen.oillessgt.OillessGTMod.OILLESS_LOGGER;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.DISABLE_DECOMPOSITION;
import static com.gregtechceu.gtceu.api.registry.GTRegistries.BEDROCK_FLUID_DEFINITIONS;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

public class ProgressionPatches {

    public static Material Acetaldehyde;

    public static void registerMaterials() {
        OILLESS_LOGGER.info("Adding custom materials");
        Acetaldehyde = new Material.Builder(OillessGTMod.id("acetaldehyde"))
                .gas()
                .color(0xbf7f5f)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 2, Hydrogen, 4, Oxygen, 1)
                .buildAndRegister();
    }

    public static void addRecipes(Consumer<FinishedRecipe> provider) {
        OILLESS_LOGGER.info("Adding custom recipes");

        if (OillessConfiguration.INSTANCE.addLargePyrolyser) {
            OillessRecipes.addInertGasBoostRecipes(provider);
            OillessRecipes.addLargePyrolyserRecipes(provider);
        }
        if (OillessConfiguration.INSTANCE.addGreenhouse) {
            OillessRecipes.addGreenhouseRecipes(provider);
        }
        OillessRecipes.addButadieneSynthesisRecipes(provider);
        OillessRecipes.addMiscReplacementRecipes(provider);
    }

    public static void removeRecipes(Consumer<ResourceLocation> provider) {
        provider.accept(GTCEu.id("centrifuge/soul_sand_separation"));
        provider.accept(GTCEu.id("centrifuge/oilsands_ore_separation"));
        provider.accept(GTCEu.id("centrifuge/oilsands_dust_separation"));
    }

    public static boolean shouldRemoveRecipe(GTRecipeBuilder recipe) {
        boolean pesterThis = false;
        if (recipe.id.toString().contains("gtceu_oil_heavy")) { //testing BS
            OILLESS_LOGGER.info("yo we got an um…uh…heavy oil recipe?");
            OILLESS_LOGGER.info("{}", recipe.id);
            pesterThis = true;
        }
        for (var oilFluid : OIL_FLUIDS) {
            var stack = RecipeBuilderHelper.stringToFluidStack(oilFluid);
            // check inputs
            for (var fluidIngredient : RecipeBuilderHelper.getInputFluids(recipe)) {
                if (pesterThis) { //more testing BS
                    OILLESS_LOGGER.info(fluidIngredient);
                    //OILLESS_LOGGER.info(fluidIngredient.getStacks());
                    OILLESS_LOGGER.info(Arrays.stream(fluidIngredient.getStacks()).map(s->s.getDisplayName().getString()).toList());
                    OILLESS_LOGGER.info(fluidIngredient.test(OilHeavy.getFluid(1)));
                }
                if (fluidIngredient.test(stack)) return true;
            }
            // check outputs
            for (var fluidIngredient : RecipeBuilderHelper.getOutputFluids(recipe)) {
                if (fluidIngredient.test(stack)) return true;
            }
        }
        return false;
    }

    public static Set<String> OIL_FLUIDS = new HashSet<>();
    public static final String[] CRACKING_TYPES = { "hydro_cracked", "steam_cracked" };
    public static final String[] CRACKING_DEGREES = { "lightly", "severely" };
    /** In the same order GT does them—which is to say, <emph>disappointingly</emph> close to an actual order. */
    public static final String[] CRACKABLE_HYDROCARBONS = { "ethane", "ethylene", "propene", "propane", "butane",
            "butene", "butadiene" };

    private static void loadOilItems() {
        OILLESS_LOGGER.info("Listing the fluids to erase from memory…");
        if (!OIL_FLUIDS.isEmpty()) return;

        // things called oil
        OIL_FLUIDS.add("gtceu:oil");
        OIL_FLUIDS.add("gtceu:oil_heavy");
        OIL_FLUIDS.add("gtceu:oil_light");
        OIL_FLUIDS.add("gtceu:raw_oil");

        // things that are basically oil
        OIL_FLUIDS.add("gtceu:natural_gas");
        OIL_FLUIDS.add("gtceu:lpg");

        // direct oil products
        OIL_FLUIDS.add("gtceu:sulfuric_light_fuel");
        OIL_FLUIDS.add("gtceu:sulfuric_heavy_fuel");
        OIL_FLUIDS.add("gtceu:sulfuric_naphtha");
        OIL_FLUIDS.add("gtceu:sulfuric_gas");

        // desulfurized oil products
        OIL_FLUIDS.add("gtceu:light_fuel");
        OIL_FLUIDS.add("gtceu:heavy_fuel");
        OIL_FLUIDS.add("gtceu:naphtha");
        OIL_FLUIDS.add("gtceu:refinery_gas");

        // cracked things
        for (String type : CRACKING_TYPES) {
            for (String degree : CRACKING_DEGREES) {
                OIL_FLUIDS.add("gtceu:%s_%s_light_fuel".formatted(degree, type));
                OIL_FLUIDS.add("gtceu:%s_%s_heavy_fuel".formatted(degree, type));
                OIL_FLUIDS.add("gtceu:%s_%s_naphtha".formatted(degree, type));
                OIL_FLUIDS.add("gtceu:%s_%s_gas".formatted(degree, type));
            }
            // do i really wanna disable these? idk
            for (String hydrocarbon : CRACKABLE_HYDROCARBONS) {
                OIL_FLUIDS.add("gtceu:%s_%s".formatted(type, hydrocarbon));
            }
        }
    }

    public static void modifyMaterials() {
        loadOilItems();
    }

    public static void removeOilVeins() {
        BEDROCK_FLUID_DEFINITIONS.remove(GTCEu.id("heavy_oil_deposit"));
        BEDROCK_FLUID_DEFINITIONS.remove(GTCEu.id("light_oil_deposit"));
        BEDROCK_FLUID_DEFINITIONS.remove(GTCEu.id("oil_deposit"));
        BEDROCK_FLUID_DEFINITIONS.remove(GTCEu.id("raw_oil_deposit"));
        BEDROCK_FLUID_DEFINITIONS.remove(GTCEu.id("natural_gas_deposit"));
        BEDROCK_FLUID_DEFINITIONS.remove(GTCEu.id("nether_natural_gas_deposit"));
    }

    public static void initLang(RegistrateLangProvider provider) {
        // replace(provider, "item.gtceu.oilsands_dust", "Tar-Rich Sands");
    }
}
