package com.daboxen.oillessgt.recipe;

import com.daboxen.oillessgt.OillessGTMod;

import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.providers.RegistrateLangProvider;

import static com.daboxen.oillessgt.OillessGTMod.OILLESS_LOGGER;

public class OillessRecipeConditions {

    public static RecipeConditionType<SubstructureCondition> SUBSTRUCTURE = register("substructure",
            SubstructureCondition::new, SubstructureCondition.CODEC);

    private static <T extends RecipeCondition<T>> RecipeConditionType<T> register(String name,
                                                                                  RecipeConditionType.ConditionFactory<T> factory,
                                                                                  Codec<T> codec) {
        return GTRegistries.RECIPE_CONDITIONS.register(OillessGTMod.id(name),
                new RecipeConditionType<>(factory, codec));
    }

    public static void init() {
        OILLESS_LOGGER.debug("Adding custom recipe conditions");
    }

    public static void initLang(RegistrateLangProvider provider) {
        provider.add("recipe.condition.substructure.tooltip", "Requires substructure '%s'");
    }
}
