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

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;

import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class LargePyrolyserMachine extends CoilWorkableElectricMultiblockMachine {

    public static final Int2ObjectMap<Pair<FluidIngredient, ModifierFunction>> inertGases = new Int2ObjectAVLTreeMap<>(Collections.reverseOrder());
    static {
        inertGases.put(5, new Pair<>(BlastProperty.GasTier.HIGHEST.getFluid(5), ModifierFunction.builder() //50 mB/t
                .durationMultiplier(.2)
                .eutMultiplier(.5)
                .build()));
        inertGases.put(4, new Pair<>(BlastProperty.GasTier.HIGHER.getFluid(4), ModifierFunction.builder() //100 mB/t
                .durationMultiplier(.3)
                .eutMultiplier(.8)
                .build()));
        inertGases.put(3, new Pair<>(BlastProperty.GasTier.HIGH.getFluid(4), ModifierFunction.builder() //200 mB/t
                .durationMultiplier(.4)
                .eutMultiplier(1)
                .build()));
        inertGases.put(2, new Pair<>(BlastProperty.GasTier.MID.getFluid(5), ModifierFunction.builder() //500 mB/t
                .durationMultiplier(.4)
                .eutMultiplier(1.2)
                .build()));
        inertGases.put(1, new Pair<>(BlastProperty.GasTier.LOW.getFluid(1), ModifierFunction.builder() //1000 mB/t
                .durationMultiplier(.5)
                .eutMultiplier(1.5)
                .build()));
    }

    @Getter
    private int currentGasTier = 0;
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
    }

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();

        checkInertGas();

        return value;
    }

    public void checkInertGas() {
        boolean usingInertGas = false;
        for (Int2ObjectMap.Entry<Pair<FluidIngredient, ModifierFunction>> inertGasMapUnit : inertGases.int2ObjectEntrySet()) {
            int gasID = inertGasMapUnit.getIntKey();
            Pair<FluidIngredient, ModifierFunction> inertGasInfo = inertGasMapUnit.getValue();
            FluidIngredient ingredient = inertGasInfo.getFirst();
            GTRecipe consumptionRecipe = GTRecipeBuilder.ofRaw().inputFluids(ingredient).buildRawRecipe();
            ModifierFunction function = inertGasInfo.getSecond();
            boolean recipeWorked = RecipeHelper.matchRecipe(this, consumptionRecipe).isSuccess() &&
                    RecipeHelper.handleRecipeIO(this, consumptionRecipe, IO.IN, this.recipeLogic.getChanceCaches())
                            .isSuccess();
            if (recipeWorked) {
                setFluidBoostType(ingredient.getStacks()[0]);
                currentGasTier = gasID;
                currentInertGasModifier = function;
                usingInertGas = true;
                break;
            }
        }
        if (!usingInertGas) {
            currentGasTier = 0;
            setFluidBoostType(FluidStack.EMPTY);
            currentInertGasModifier = ModifierFunction.IDENTITY;
        }
    }

    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof LargePyrolyserMachine pyrolyserMachine)) {
            return RecipeModifier.nullWrongType(LargePyrolyserMachine.class, machine);
        }
        if (recipe.data.contains("minimum_gas_tier")) {
            int minTier = recipe.data.getInt("minimum_gas_tier");
            int actualTier = 0;

            //we can't trust getCurrentGasTier() because it only updates while running. we have to check it manually!
            for (Int2ObjectMap.Entry<Pair<FluidIngredient, ModifierFunction>> inertGasMapUnit : inertGases.int2ObjectEntrySet()) {
                Pair<FluidIngredient, ModifierFunction> inertGasInfo = inertGasMapUnit.getValue();
                FluidIngredient ingredient = inertGasInfo.getFirst();
                GTRecipe consumptionRecipe = GTRecipeBuilder.ofRaw().inputFluids(ingredient).buildRawRecipe();
                boolean recipeWorked = RecipeHelper.matchRecipe(pyrolyserMachine, consumptionRecipe).isSuccess();
                if (recipeWorked) {
                    actualTier = inertGasMapUnit.getIntKey();
                    break;
                }
            }

            if (actualTier < minTier) {
                return ModifierFunction.cancel(Component.translatable("oillessgt.multiblock.large_pyrolyse_oven.low_gas_tier"));
            }
        }

        //TODO: maybe implement batching/parallels/stuff in here instead of slapping them on top?
        return pyrolyserMachine.currentInertGasModifier;
    }
}
