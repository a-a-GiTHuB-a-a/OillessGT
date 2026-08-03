package com.daboxen.oillessgt.machines;

import brachy.modularui.api.drawable.Text;
import brachy.modularui.value.sync.BooleanSyncValue;
import brachy.modularui.value.sync.IntSyncValue;
import com.daboxen.oillessgt.config.OillessConfiguration;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.multiblock.Predicates;
import com.gregtechceu.gtceu.api.multiblock.pattern.MultiblockPatternBuilder;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;

import static com.daboxen.oillessgt.OillessGTMod.LOGGER;
import static com.daboxen.oillessgt.OillessGTMod.OILLESS_REGISTRATE;
import static com.gregtechceu.gtceu.api.multiblock.Predicates.blocks;
import static com.gregtechceu.gtceu.api.multiblock.util.RelativeDirection.*;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;
import static com.gregtechceu.gtceu.common.data.GCYMBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.*;

public class OillessMachines {


    public static MultiblockMachineDefinition LARGE_PYROLYSER;

    public static void init(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
        LOGGER.info("Registering machines…");

        if (OillessConfiguration.INSTANCE.addLargePyrolyser) {
            LARGE_PYROLYSER = OILLESS_REGISTRATE
                    .multiblock("large_pyrolyser", CoilWorkableElectricMultiblockMachine::new)
                    .rotationState(RotationState.NONE)
                    //.recipeType(GTRecipeTypes.PYROLYSE_RECIPES)
                    .recipeModifiers(GTRecipeModifiers::pyrolyseOvenOverclock, BATCH_MODE, GTRecipeModifiers::hatchParallel)
                    .appearanceBlock(MACHINE_CASING_ULV)
                    .pattern(definition -> MultiblockPatternBuilder.start(UP, FRONT, RIGHT)
                            .slice("               ", "               ", "               ", "               ", "    TTTTTTT    ", "    TTTTTTT    ", "    TTTTTTT    ", "    TTTSTTT    ", "    TTTTTTT    ", "    TTTTTTT    ", "    TTTTTTT    ", "               ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "               ", "    TTTTTTT    ", "    T#####T    ", "    T#####T    ", "    T#####T    ", "    T#####T    ", "    T#####T    ", "    TTTTTTT    ", "               ", "               ", "               ", "               ")
                            .where('S', Predicates.controller(blocks(definition.get())))
                            .where('T', Predicates.blocks(CASING_HIGH_TEMPERATURE_SMELTING.get()))
                            .where('C', Predicates.heatingCoils())
                            .where('#', Predicates.air())
                            .where(' ', Predicates.any())
                            .build())
                    .allowFlip(false)
                    .workableCasingModel(GTCEu.id("block/casings/voltage/ulv/side"),
                            GTCEu.id("block/multiblock/pyrolyse_oven"))
                    .tooltips(Component.translatable("gtceu.machine.pyrolyse_oven.tooltip.1"))
                    .additionalDisplay((controller, syncManager) -> {
                        if (!(controller instanceof CoilWorkableElectricMultiblockMachine coilMachine))
                            return Collections.emptyList();
                        BooleanSyncValue isFormed = syncManager.getOrCreateSyncHandler("isFormed", BooleanSyncValue.class,
                                () -> new BooleanSyncValue(controller::isFormed));
                        IntSyncValue coilTier = syncManager.getOrCreateSyncHandler("coilTier", IntSyncValue.class,
                                () -> new IntSyncValue(() -> coilMachine.getCoilTier()));

                        return Collections.singletonList(Text
                                .dynamic(() -> Component.translatable("gtceu.multiblock.pyrolyse_oven.speed",
                                        coilTier.getIntValue() == 0 ? 75 : 50 * (coilTier.getIntValue() + 1)))
                                .asWidget().setEnabledIf(w -> isFormed.getBoolValue()));
                    })
                    .register();
        }
    }
}
