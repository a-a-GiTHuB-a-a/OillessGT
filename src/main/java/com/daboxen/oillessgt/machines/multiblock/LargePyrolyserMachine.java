package com.daboxen.oillessgt.machines.multiblock;

import com.gregtechceu.gtceu.api.blockentity.BlockEntityCreationInfo;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.api.sync_system.annotations.SaveField;
import com.gregtechceu.gtceu.api.sync_system.annotations.SyncToClient;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LargePyrolyserMachine extends CoilWorkableElectricMultiblockMachine {

    public static final List<Pair<FluidIngredient, ModifierFunction>> inertGases = new ArrayList<>(5);

    @Getter
    @SaveField
    @SyncToClient
    private @NotNull FluidStack currentFluidBoostType = FluidStack.EMPTY;
    private @NotNull ModifierFunction currentInertGasModifier = ModifierFunction.IDENTITY;

    public void setFluidBoostType(FluidStack stack) {
        this.currentFluidBoostType = stack;
        syncDataHolder.markClientSyncFieldDirty("currentFluidBoostType");
    }

    public LargePyrolyserMachine(BlockEntityCreationInfo info) {
        super(info);
        inertGases.add(new Pair<>(BlastProperty.GasTier.HIGHEST.getFluid(50), ModifierFunction.builder()
                .durationMultiplier(.2)
                .eutMultiplier(.5)
                .build()));
        inertGases.add(new Pair<>(BlastProperty.GasTier.HIGHER.getFluid(100), ModifierFunction.builder()
                .durationMultiplier(.3)
                .eutMultiplier(.8)
                .build()));
        inertGases.add(new Pair<>(BlastProperty.GasTier.HIGH.getFluid(200), ModifierFunction.builder()
                .durationMultiplier(.4)
                .eutMultiplier(1)
                .build()));
        inertGases.add(new Pair<>(BlastProperty.GasTier.MID.getFluid(500), ModifierFunction.builder()
                .durationMultiplier(.4)
                .eutMultiplier(1.2)
                .build()));
        inertGases.add(new Pair<>(BlastProperty.GasTier.LOW.getFluid(1000), ModifierFunction.builder()
                .durationMultiplier(.5)
                .eutMultiplier(1.5)
                .build()));
    }

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();

        boolean usingInertGas = false;

        for (Pair<FluidIngredient, ModifierFunction> inertGasInfo : inertGases) {
            FluidIngredient ingredient = inertGasInfo.getFirst();
            GTRecipe consumptionRecipe = GTRecipeBuilder.ofRaw().inputFluids(ingredient).buildRawRecipe();
            ModifierFunction function = inertGasInfo.getSecond();
            boolean recipeWorked = RecipeHelper.matchRecipe(this, consumptionRecipe).isSuccess() &&
                    RecipeHelper.handleRecipeIO(this, consumptionRecipe, IO.IN, this.recipeLogic.getChanceCaches())
                            .isSuccess();
            if (recipeWorked) {
                setFluidBoostType(ingredient.getStacks()[0]);
                currentInertGasModifier = function;
                usingInertGas = true;
                break;
            }
        }
        if (!usingInertGas) {
            setFluidBoostType(FluidStack.EMPTY);
            currentInertGasModifier = ModifierFunction.IDENTITY;
        }

        return value;
    }

    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof LargePyrolyserMachine pyrolyserMachine)) {
            return RecipeModifier.nullWrongType(LargePyrolyserMachine.class, machine);
        }

        return pyrolyserMachine.currentInertGasModifier; //TODO: maybe implement batching/parallels/stuff in here instead of slapping them on top?
    }
}