package com.daboxen.oillessgt.jei;

import com.daboxen.oillessgt.OillessGTMod;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.daboxen.oillessgt.machines.OillessMachines.LARGE_PYROLYSER;
import static com.daboxen.oillessgt.recipe.OillessRecipeTypes.INERT_GAS_BOOST_RECIPES;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class OillessJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return OillessGTMod.id("jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        if (GTCEu.Mods.isREILoaded() || GTCEu.Mods.isEMILoaded()) return;
        registration.addRecipeCatalyst(LARGE_PYROLYSER.asStack(), new RecipeType<>(INERT_GAS_BOOST_RECIPES.getCategory().registryKey, GTRecipe.class));
    }
}
