package com.daboxen.oillessgt.recipe;

import com.gregtechceu.gtceu.api.recipe.gui.RecipeUIModifier;

import net.minecraft.network.chat.Component;

import brachy.modularui.widgets.TextWidget;
import brachy.modularui.widgets.layout.Flow;

public class OillessUIModifiers {

    public static final RecipeUIModifier INERT_GAS_REQUIREMENT_INFO = (recipe, widget) -> {
        if (recipe.data.contains("minimum_gas_tier")) {
            int minimumGasTier = recipe.data.getInt("minimum_gas_tier");
            /*
             * FluidIngredient requiredGasBoost = FluidIngredient.EMPTY;
             * for (Int2ObjectMap.Entry<Pair<FluidIngredient, ModifierFunction>> entry :
             * LargePyrolyserMachine.inertGases.int2ObjectEntrySet()) {
             * if (entry.getIntKey() == minimumGasTier) {
             * requiredGasBoost = entry.getValue().getFirst();
             * break;
             * }
             * }
             */

            Flow gasInfoRow = Flow.row().coverChildrenHeight();

            gasInfoRow.child(new TextWidget<>(
                    Component.translatable("oillessgt.recipe.inert_gas_boost.minimum", minimumGasTier)));

            widget.textComponents.child(gasInfoRow);
        }
    };
}
