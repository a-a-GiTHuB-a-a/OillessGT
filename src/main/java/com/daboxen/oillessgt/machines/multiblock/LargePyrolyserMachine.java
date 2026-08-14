package com.daboxen.oillessgt.machines.multiblock;

import com.gregtechceu.gtceu.api.blockentity.BlockEntityCreationInfo;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.api.sync_system.annotations.SaveField;
import com.gregtechceu.gtceu.api.sync_system.annotations.SyncToClient;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LargePyrolyserMachine extends CoilWorkableElectricMultiblockMachine {
    @Getter
    private static final Int2ObjectMap<FluidIngredient> boostFluids = new Int2ObjectAVLTreeMap<>();
    @Getter
    private static final Int2ObjectMap<ModifierFunction> boostEffects = new Int2ObjectAVLTreeMap<>();
    static {
        boostFluids.put(5, BlastProperty.GasTier.HIGHEST.getFluid(5)); //50 mB/t
        boostEffects.put(5, ModifierFunction.builder()
                .durationMultiplier(.2)
                .eutMultiplier(.5)
                .build());
        boostFluids.put(4, BlastProperty.GasTier.HIGHER.getFluid(4)); //100 mB/t
        boostEffects.put(4, ModifierFunction.builder()
                .durationMultiplier(.3)
                .eutMultiplier(.8)
                .build());
        boostFluids.put(3, BlastProperty.GasTier.HIGH.getFluid(4)); //200 mB/t
        boostEffects.put(3, ModifierFunction.builder()
                .durationMultiplier(.4)
                .eutMultiplier(1)
                .build());
        boostFluids.put(2, BlastProperty.GasTier.MID.getFluid(5)); //500 mB/t
        boostEffects.put(2, ModifierFunction.builder()
                .durationMultiplier(.4)
                .eutMultiplier(1.2)
                .build());
        boostFluids.put(1, BlastProperty.GasTier.LOW.getFluid(1)); //1000 mB/t
        boostEffects.put(1, ModifierFunction.builder()
                .durationMultiplier(.5)
                .eutMultiplier(1.5)
                .build());
        boostFluids.put(0, FluidIngredient.EMPTY);
        boostEffects.put(0, ModifierFunction.IDENTITY);
    }

    public static IntList getTiers() {
        return IntList.of(5, 4, 3, 2, 1, 0);
    }
    public static FluidIngredient getBoostFluid(int tier) {
        return getBoostFluids().get(tier);
    }
    public static ModifierFunction getBoostEffect(int tier) {
        return getBoostEffects().get(tier);
    }

    @Getter
    @SaveField
    @SyncToClient
    private int currentGasTier = 0;

    public void setCurrentGasTier(int tier) {
        currentGasTier = tier;
        syncDataHolder.markClientSyncFieldDirty("currentGasTier");
    }

    public LargePyrolyserMachine(BlockEntityCreationInfo info) {
        super(info);
    }

    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof LargePyrolyserMachine pyrolyserMachine)) {
            return RecipeModifier.nullWrongType(LargePyrolyserMachine.class, machine);
        }
        if (recipe.data.contains("minimum_gas_tier")) {
            int minTier = recipe.data.getInt("minimum_gas_tier");
            int actualTier = 0;

            for (int tier : getTiers()) {
                FluidIngredient ingredient = getBoostFluids().get(tier);
                GTRecipe consumptionRecipe = GTRecipeBuilder.ofRaw().inputFluids(ingredient).buildRawRecipe();
                boolean recipeWorked = RecipeHelper.matchRecipe(pyrolyserMachine, consumptionRecipe).isSuccess();
                if (recipeWorked) {
                    actualTier = tier;
                    break;
                }
            }

            if (actualTier < minTier) {
                return ModifierFunction.cancel(Component.translatable("oillessgt.multiblock.large_pyrolyse_oven.low_gas_tier"));
            }
            pyrolyserMachine.setCurrentGasTier(actualTier);
        } else {
            pyrolyserMachine.setCurrentGasTier(0);
        }

        //TODO: maybe implement batching/parallels/stuff in here instead of slapping them on top?
        return getBoostEffect(pyrolyserMachine.getCurrentGasTier()).andThen(innerRecipe -> {
            var fluid = getBoostFluid(pyrolyserMachine.getCurrentGasTier());
            if (fluid.isEmpty()) return innerRecipe;
            return GTRecipeBuilder.ofRaw().copy(innerRecipe.id).perTick(true).inputFluids(fluid).buildRawRecipe();
        });
    }
}
