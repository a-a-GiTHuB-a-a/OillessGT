package com.daboxen.oillessgt.recipe;

import brachy.modularui.api.drawable.Text;
import brachy.modularui.widgets.TextWidget;
import brachy.modularui.widgets.layout.Flow;
import com.daboxen.oillessgt.machines.multiblock.LargePyrolyserMachine;
import com.gregtechceu.gtceu.api.recipe.gui.RecipeUIModifier;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.network.chat.Component;

public class OillessUIModifiers {
    public static final RecipeUIModifier INERT_GAS_INFO = (recipe, widget) -> {
        if (recipe.data.contains("minimum_gas_tier")) {
            int minimumGasTier = recipe.data.getInt("minimum_gas_tier");
            FluidIngredient requiredGasBoost = FluidIngredient.EMPTY;
            for (Int2ObjectMap.Entry<Pair<FluidIngredient, ModifierFunction>> entry : LargePyrolyserMachine.inertGases.int2ObjectEntrySet()) {
                if (entry.getIntKey() == minimumGasTier) {
                    requiredGasBoost = entry.getValue().getFirst();
                    break;
                }
            }

            Flow gasInfoRow = Flow.row().coverChildrenHeight();

            gasInfoRow.child(new TextWidget<>(Text.lang("oillessgt.recipe.inert_gas_boost.minimum",
                    Component.translatable(requiredGasBoost.getStacks()[0].getTranslationKey()))));

            widget.textComponents.child(gasInfoRow);
        }
    };
}
