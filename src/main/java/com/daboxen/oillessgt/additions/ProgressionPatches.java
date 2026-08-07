package com.daboxen.oillessgt.additions;

import com.daboxen.oillessgt.OillessGTMod;
import com.daboxen.oillessgt.api.RecipeBuilderHelper;

import com.daboxen.oillessgt.recipe.OillessRecipes;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import com.tterrag.registrate.providers.RegistrateLangProvider;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static com.daboxen.oillessgt.OillessGTMod.OILLESS_LOGGER;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.DISABLE_DECOMPOSITION;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.api.registry.GTRegistries.BEDROCK_FLUID_DEFINITIONS;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.gregtechceu.gtceu.data.lang.LangHandler.replace;

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

        OillessRecipes.addButadieneSynthesisRecipes(provider);
        OillessRecipes.addLargePyrolyserRecipes(provider);

        CENTRIFUGE_RECIPES.recipeBuilder("soul_sand_separation_oilless").duration(200).EUt(80)
                .inputItems(Blocks.SOUL_SAND.asItem())
                .chancedOutput(new ItemStack(Blocks.SAND), 9250)
                .chancedOutput(dust, Saltpeter, 2250)
                .chancedOutput(dust, Coal, 225)
                .outputFluids(CoalTar.getFluid(80))
                .save(provider);

        CENTRIFUGE_RECIPES.recipeBuilder("oilsands_ore_separation_oilless")
                .inputItems(ore, Oilsands)
                .chancedOutput(new ItemStack(Blocks.SAND), 7500)
                .outputFluids(CoalTar.getFluid(2000))
                .duration(200).EUt(30).save(provider);

        CENTRIFUGE_RECIPES.recipeBuilder("oilsands_dust_separation_oilless")
                .inputItems(dust, Oilsands)
                .chancedOutput(new ItemStack(Blocks.SAND), 7500)
                .outputFluids(CoalTar.getFluid(2000))
                .duration(200).EUt(30).save(provider);
    }

    public static void removeRecipes(Consumer<ResourceLocation> provider) {
        provider.accept(GTCEu.id("centrifuge/soul_sand_separation"));
        provider.accept(GTCEu.id("centrifuge/oilsands_ore_separation"));
        provider.accept(GTCEu.id("centrifuge/oilsands_dust_separation"));
    }

    public static boolean shouldRemoveRecipe(GTRecipeBuilder recipe) {
        for (var oilFluid : OIL_FLUIDS) {
            var stack = RecipeBuilderHelper.stringToFluidStack(oilFluid);
            // check inputs
            for (var fluidIngredient : RecipeBuilderHelper.getInputFluids(recipe)) {
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
        replace(provider, "material.gtceu.oilsands", "Tar-Rich Sands");
        replace(provider, "item.gtceu.oilsands_dust", "Tar-Rich Sands");
    }
}
