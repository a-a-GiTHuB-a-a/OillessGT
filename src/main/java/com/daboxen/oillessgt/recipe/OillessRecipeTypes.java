package com.daboxen.oillessgt.recipe;

import brachy.modularui.api.drawable.Text;
import com.daboxen.oillessgt.OillessGTMod;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.recipe.GTRecipeSerializer;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;
import com.gregtechceu.gtceu.common.mui.GTGuiTextures;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeType;

import static com.daboxen.oillessgt.OillessGTMod.OILLESS_LOGGER;
import static com.daboxen.oillessgt.machines.OillessMachines.LARGE_PYROLYSER;


public class OillessRecipeTypes {
    public static final GTRecipeType LARGE_PYROLYSE_RECIPES = register("large_pyrolyser", GTRecipeTypes.MULTIBLOCK).setMaxIOSize(2, 1, 1, 1)
            .setEUIO(IO.IN)
            .UI(builder -> builder.setProgressBar(GTGuiTextures.PROGRESS_ARROW)
                    .addRecipeUIModifier(OillessUIModifiers.INERT_GAS_REQUIREMENT_INFO))
            .setSound(GTSoundEntries.FIRE);
    public static final GTRecipeType INERT_GAS_BOOST_RECIPES = register("inert_gas_boost", GTRecipeTypes.DUMMY).setMaxIOSize(0, 0, 1, 0)
            .setEUIO(IO.NONE)
            .UI(builder -> builder.setProgressBar(GTGuiTextures.PROGRESS_ARROW)
                    .addRecipeUIModifier((recipe, widget) -> widget
                            .textComponents.child(Text.of(Component.translatable("oillessgt.recipe.inert_gas_boost.tier", recipe.data.getInt("gas_tier"))).asWidget()))
                    .addRecipeUIModifier((recipe, widget) -> widget
                            .textComponents.child(Text.of(Component.translatable("oillessgt.recipe.inert_gas_boost.eut_multiplier", recipe.data.getString("eut_multiplier"))).asWidget()))
                    .addRecipeUIModifier((recipe, widget) -> widget
                            .textComponents.child(Text.of(Component.translatable("oillessgt.recipe.inert_gas_boost.duration_multiplier", recipe.data.getString("duration_multiplier"))).asWidget())))
            .setIconSupplier(() -> LARGE_PYROLYSER.asStack());

    public static void init() {
        OILLESS_LOGGER.info("Registering recipe types…");
    }

    public static void initLang(RegistrateLangProvider provider) {
        provider.add("oillessgt.large_pyrolyser", "Large Pyrolyser");
        provider.add("oillessgt.inert_gas_boost", "Inert Gas Boosting");
        provider.add("oillessgt.recipe.inert_gas_boost.minimum", "Minimum Inert Gas Tier: %d");
        provider.add("oillessgt.recipe.inert_gas_boost.tier", "Gas Tier: %d");
        provider.add("oillessgt.recipe.inert_gas_boost.eut_multiplier", "EU/t Multiplier: %sx");
        provider.add("oillessgt.recipe.inert_gas_boost.duration_multiplier", "Duration Multiplier: %sx");
    }

    public static GTRecipeType register(String name, String group, RecipeType<?>... proxyRecipes) {
        GTRecipeType recipeType = new GTRecipeType(OillessGTMod.id(name), group, proxyRecipes);
        GTRegistries.register(BuiltInRegistries.RECIPE_TYPE, recipeType.registryName, recipeType);
        GTRegistries.register(BuiltInRegistries.RECIPE_SERIALIZER, recipeType.registryName, new GTRecipeSerializer());
        GTRegistries.RECIPE_TYPES.register(recipeType.registryName, recipeType);
        return recipeType;
    }
}