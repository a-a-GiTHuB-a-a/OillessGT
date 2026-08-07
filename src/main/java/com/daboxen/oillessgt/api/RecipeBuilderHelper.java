package com.daboxen.oillessgt.api;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class RecipeBuilderHelper {

    public static List<FluidIngredient> getInputFluids(GTRecipeBuilder builder) {
        return builder.input.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList()).stream()
                .map((content) -> FluidRecipeCapability.CAP.of(content.content())).toList();
    }

    public static List<FluidIngredient> getOutputFluids(GTRecipeBuilder builder) {
        return builder.output.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList()).stream()
                .map((content) -> FluidRecipeCapability.CAP.of(content.content())).toList();
    }

    public static FluidStack stringToFluidStack(String id) {
        var nbt = new CompoundTag();
        nbt.putString("FluidName", id);
        nbt.putInt("Amount", 1000);
        return FluidStack.loadFluidStackFromNBT(nbt);
    }
}
