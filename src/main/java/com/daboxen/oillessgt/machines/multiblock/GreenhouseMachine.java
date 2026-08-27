package com.daboxen.oillessgt.machines.multiblock;

import com.gregtechceu.gtceu.api.blockentity.BlockEntityCreationInfo;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.multiblock.error.BlockMatchingError;
import com.gregtechceu.gtceu.api.multiblock.pattern.PatternState;

import org.jetbrains.annotations.NotNull;

import static com.daboxen.oillessgt.OillessGTMod.OILLESS_LOGGER;

public class GreenhouseMachine extends WorkableElectricMultiblockMachine {

    public GreenhouseMachine(BlockEntityCreationInfo info) {
        super(info);
    }

    @Override
    public void createStructurePatterns() {
        super.createStructurePatterns();
        patternStates.put("arboretum", new PatternState());
    }

    public String getInternalStructureName() {
        var arboretum = patternStates.get("arboretum");
        if (arboretum.isFormed()) {
            OILLESS_LOGGER.info("ARBORETUM FORMED!!!");
            return "arboretum";
        }
        if (arboretum.hasErrors()) {
            var error = patternStates.get("arboretum").getErrors().get(0);
            String errorName = "position %s".formatted(error.getPos());
            if (error instanceof BlockMatchingError blockError) {
                errorName = "blocks %s at position %s".formatted(blockError.getBlocks(), blockError.getPos());
            }
            OILLESS_LOGGER.info("Error: {}", errorName);
        }
        OILLESS_LOGGER.info("guh {}", arboretum.getState());
        arboretum.setShouldUpdate(true);
        return "none"; // lmaoooo skill issue
    }

    @Override
    public void formStructure(@NotNull String substructureName) {
        super.formStructure(substructureName);
    }
}
