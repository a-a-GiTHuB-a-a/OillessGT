package com.daboxen.oillessgt.additions;

import com.daboxen.oillessgt.OillessGTMod;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.DISABLE_DECOMPOSITION;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
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

    public static final Set<String> OIL_ITEMS = new HashSet<>();
    public static final String[] CRACKING_TYPES = {"hydro_cracked", "steam_cracked"};

    public static void loadOilItems() {
        //things called oil
        OIL_ITEMS.add("gtceu:oil");
        OIL_ITEMS.add("gtceu:heavy_oil");
        OIL_ITEMS.add("gtceu:light_oil");
        OIL_ITEMS.add("gtceu:raw_oil");

        //things that are basically oil
        OIL_ITEMS.add("gtceu:natural_gas");

        //direct oil products
        OIL_ITEMS.add("gtceu:sulfuric_light_fuel");
        OIL_ITEMS.add("gtceu:sulfuric_heavy_fuel");
        OIL_ITEMS.add("gtceu:sulfuric_naphtha");
        OIL_ITEMS.add("gtceu:sulfuric_gas");

        //desulfurized oil products
        OIL_ITEMS.add("gtceu:light_fuel");
        OIL_ITEMS.add("gtceu:heavy_fuel");
        OIL_ITEMS.add("gtceu:naphtha");
        OIL_ITEMS.add("gtceu:refinery_gas");
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

            if (OIL_ITEMS.contains(resStr)) return true;
        }
        String rawJSON = obj.toString();
        for (String inputItem : OIL_ITEMS) {
            if (rawJSON.contains("\"item\":\"" + inputItem + "\"") ||
                    rawJSON.contains("\"item\": \"" + inputItem + "\""))
                return true;
        }
        for (String crackingType : CRACKING_TYPES) {
            if (rawJSON.contains(crackingType)) return true;
        }
        return false;
    }
}