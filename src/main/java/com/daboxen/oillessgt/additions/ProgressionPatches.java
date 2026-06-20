package com.daboxen.oillessgt.additions;

import com.daboxen.oillessgt.OillessGTMod;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.DISABLE_DECOMPOSITION;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.api.registry.GTRegistries.BEDROCK_FLUID_DEFINITIONS;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

public class ProgressionPatches {
    public static Material Acetaldehyde;

    public static void registerMaterials() {
        OillessGTMod.LOGGER.info("Adding custom materials");
        Acetaldehyde = new Material.Builder(OillessGTMod.id("acetaldehyde"))
                .gas()
                .color(0xbf7f5f)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 2, Hydrogen, 4, Oxygen, 1)
                .buildAndRegister();
    }

    public static void addRecipes(Consumer<FinishedRecipe> provider) {
        OillessGTMod.LOGGER.info("Adding custom recipes");
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

    public static Set<String> OIL_FLUIDS = new HashSet<>();
    public static final String[] CRACKING_TYPES = {"hydro_cracked", "steam_cracked"};
    public static final String[] CRACKING_DEGREES = {"lightly", "severely"};
    public static final String[] CRACKABLE_HYDROCARBONS = {"ethane", "ethylene", "propene", "propane", "butane", "butene", "butadiene"}; //in the same order GT does them—which is to say, _disappointingly_ close to an actual order

    public static void loadOilItems() {
        OillessGTMod.LOGGER.info("Listing the fluids to erase from memory…");

        //things called oil
        OIL_FLUIDS.add("gtceu:oil");
        OIL_FLUIDS.add("gtceu:heavy_oil");
        OIL_FLUIDS.add("gtceu:light_oil");
        OIL_FLUIDS.add("gtceu:raw_oil");

        //things that are basically oil
        OIL_FLUIDS.add("gtceu:natural_gas");

        //direct oil products
        OIL_FLUIDS.add("gtceu:sulfuric_light_fuel");
        OIL_FLUIDS.add("gtceu:sulfuric_heavy_fuel");
        OIL_FLUIDS.add("gtceu:sulfuric_naphtha");
        OIL_FLUIDS.add("gtceu:sulfuric_gas");

        //desulfurized oil products
        OIL_FLUIDS.add("gtceu:light_fuel");
        OIL_FLUIDS.add("gtceu:heavy_fuel");
        OIL_FLUIDS.add("gtceu:naphtha");
        OIL_FLUIDS.add("gtceu:refinery_gas");

        //cracked things
        for (String type : CRACKING_TYPES) {
            for (String degree : CRACKING_DEGREES) {
                OIL_FLUIDS.add("gtceu:%s_%s_light_fuel".formatted(degree, type));
                OIL_FLUIDS.add("gtceu:%s_%s_heavy_fuel".formatted(degree, type));
                OIL_FLUIDS.add("gtceu:%s_%s_naphtha".formatted(degree, type));
                OIL_FLUIDS.add("gtceu:%s_%s_gas".formatted(degree, type));
            }
            for (String hydrocarbon : CRACKABLE_HYDROCARBONS) {
                OIL_FLUIDS.add("gtceu:%s_%s".formatted(type, hydrocarbon));
            }
        }
    }

    public static void removeOilVeins() {
        BEDROCK_FLUID_DEFINITIONS.remove(GTCEu.id("heavy_oil_deposit"));
        BEDROCK_FLUID_DEFINITIONS.remove(GTCEu.id("light_oil_deposit"));
        BEDROCK_FLUID_DEFINITIONS.remove(GTCEu.id("oil_deposit"));
        BEDROCK_FLUID_DEFINITIONS.remove(GTCEu.id("natural_gas_deposit"));
        BEDROCK_FLUID_DEFINITIONS.remove(GTCEu.id("nether_natural_gas_deposit"));
    }

    public static boolean shouldDeleteFromJSON(ResourceLocation id, JsonElement json) {
        if (!json.isJsonObject()) return false;
        JsonObject obj = json.getAsJsonObject();
        if (obj.has("result")) {
            JsonElement result = obj.get("result");
            String resStr = "";
            if (result.isJsonPrimitive()) resStr = result.getAsString();
            else if (result.isJsonObject() && result.getAsJsonObject().has("item"))
                resStr = result.getAsJsonObject().get("item").getAsString();
            if (OIL_FLUIDS.contains(resStr)) return true;
        }
        String rawJSON = obj.toString();
        for (String inputItem : OIL_FLUIDS) {
            if (rawJSON.contains("\"item\":\"" + inputItem + "\"") ||
                    rawJSON.contains("\"item\": \"" + inputItem + "\""))
                return true;
        }
        return false;
    }
}