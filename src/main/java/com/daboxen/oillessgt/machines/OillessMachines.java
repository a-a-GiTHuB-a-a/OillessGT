package com.daboxen.oillessgt.machines;

import brachy.modularui.api.drawable.Text;
import brachy.modularui.api.widget.IWidget;
import brachy.modularui.value.sync.BooleanSyncValue;
import brachy.modularui.value.sync.GenericSyncValue;
import brachy.modularui.value.sync.IntSyncValue;
import com.daboxen.oillessgt.api.RecipeBuilderHelper;
import com.daboxen.oillessgt.config.OillessConfiguration;
import com.daboxen.oillessgt.machines.multiblock.LargePyrolyserMachine;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;

import com.gregtechceu.gtceu.api.multiblock.OriginOffset;
import com.gregtechceu.gtceu.api.multiblock.Predicates;
import com.gregtechceu.gtceu.api.multiblock.pattern.MultiblockPatternBuilder;
import com.gregtechceu.gtceu.api.recipe.content.SerializerFluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.sync_system.data_transformers.ValueTransformers;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

import com.gregtechceu.gtceu.common.mui.GTByteBufAdapters;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.daboxen.oillessgt.OillessGTMod.OILLESS_LOGGER;
import static com.daboxen.oillessgt.OillessGTMod.OILLESS_REGISTRATE;
import static com.gregtechceu.gtceu.api.multiblock.Predicates.blocks;
import static com.gregtechceu.gtceu.api.multiblock.util.RelativeDirection.*;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;
import static com.gregtechceu.gtceu.common.data.GCYMBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.*;

public class OillessMachines {

    public static MultiblockMachineDefinition LARGE_PYROLYSER;

    public static void init(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
        OILLESS_LOGGER.info("Registering machines…");

        if (OillessConfiguration.INSTANCE.addLargePyrolyser) {
            LARGE_PYROLYSER = OILLESS_REGISTRATE
                    .multiblock("large_pyrolyser", LargePyrolyserMachine::new)
                    .langValue("Trunk-Ravaging Energized Extractor")
                    .rotationState(RotationState.NON_Y_AXIS)
                    .recipeType(PYROLYSE_RECIPES)
                    .recipeModifiers(LargePyrolyserMachine::recipeModifier, GTRecipeModifiers::hatchParallel, GTRecipeModifiers::pyrolyseOvenOverclock, BATCH_MODE)
                    .appearanceBlock(CASING_HIGH_TEMPERATURE_SMELTING)
                    .partAppearance((controller, part, side) -> {
                        int NEAR_BOUGHS = 10; //number chosen somewhat arbitrarily—should work
                        if ((part.getBlockPos().getY() - controller.getBlockPos().getY()) > NEAR_BOUGHS) {
                            return CASING_VIBRATION_SAFE.getDefaultState();
                        }
                        return CASING_HIGH_TEMPERATURE_SMELTING.getDefaultState();
                    })
                    .pattern(definition -> MultiblockPatternBuilder.start(UP, FRONT, RIGHT)
                            .slice("       T       ", "       T       ", "      TTT      ", "     TTTTT     ", "    TTTTTTT    ", "   TTTTTTTTT   ", "  TTTTTTTTTTT  ", "TTTTTTTETTTTTTT", "  TTTTTTTTTTT  ", "   TTTTTTTTT   ", "    TTTTTTT    ", "     TTTTT     ", "      TTT      ", "       T       ", "       T       ")
                            .slice("               ", "               ", "               ", "      TTT      ", "    TITTTIT    ", "    ICCCCCI    ", "   TTC#P#CTT   ", "   TTCP#PCTT   ", "   TTC#P#CTT   ", "    ICCCCCI    ", "    TITTTIT    ", "      TTT      ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "               ", "    TIIIIIT    ", "    ICCCCCI    ", "    IC#P#CI    ", "    ICP#PCI    ", "    IC#P#CI    ", "    ICCCCCI    ", "    TIISIIT    ", "               ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "               ", "    TGGGGGT    ", "    GCCCCCG    ", "    GC#P#CG    ", "    GCP#PCG    ", "    GC#P#CG    ", "    GCCCCCG    ", "    TGGGGGT    ", "               ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "               ", "    TGGGGGT    ", "    GCCCCCG    ", "    GC#P#CG    ", "    GCP#PCG    ", "    GC#P#CG    ", "    GCCCCCG    ", "    TGGGGGT    ", "               ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "               ", "    TGGGGGT    ", "    GCCCCCG    ", "    GC#P#CG    ", "    GCP#PCG    ", "    GC#P#CG    ", "    GCCCCCG    ", "    TGGGGGT    ", "               ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "               ", "    TGGGGGT    ", "    GCCCCCG    ", "    GC#P#CG    ", "    GCP#PCG    ", "    GC#P#CG    ", "    GCCCCCG    ", "    TGGGGGT    ", "               ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "               ", "    TGGGGGT    ", "    GCCCCCG    ", "    GC#P#CG    ", "    GCP#PCG    ", "    GC#P#CG    ", "    GCCCCCG    ", "    TGGGGGT    ", "               ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "               ", "    TGGGGGT    ", "    GCCCCCG    ", "    GC#P#CG    ", "    GCP#PCG    ", "    GC#P#CG    ", "    GCCCCCG    ", "    TGGGGGT    ", "               ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "               ", "    TGGGGGT    ", "    GCCCCCG    ", "    GC#P#CG    ", "    GCP#PCG    ", "    GC#P#CG    ", "    GCCCCCG    ", "    TGGGGGT    ", "               ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "               ", "    TGGGGGT    ", "    GCCCCCG    ", "    GC#P#CG    ", "    GCP#PCG    ", "    GC#P#CG    ", "    GCCCCCG    ", "    TGGGGGT    ", "               ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "               ", "    TGGGGGT    ", "    GCCCCCG    ", "    GC#P#CG    ", "    GCP#PCG    ", "    GC#P#CG    ", "    GCCCCCG    ", "    TGGGGGT    ", "               ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "               ", "    TGGGGGT    ", "    GCCCCCG    ", "    GC#P#CG    ", "    GCP#PCG    ", "    GC#P#CG    ", "    GCCCCCG    ", "    TGGGGGT    ", "               ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "               ", "    TGGGGGT    ", "    GCCCCCG    ", "    GC#P#CG    ", "    GCP#PCG    ", "    GC#P#CG    ", "    GCCCCCG    ", "    TGGGGGT    ", "               ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "               ", "    TGGGGGT    ", "    GCCCCCG    ", "    GC#P#CG    ", "    GCP#PCG    ", "    GC#P#CG    ", "    GCCCCCG    ", "    TGGGGGT    ", "               ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "               ", "    TGGGGGT    ", "    GCCCCCG    ", "    GC#P#CG    ", "    GCP#PCG    ", "    GC#P#CG    ", "    GCCCCCG    ", "    TGGGGGT    ", "               ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "               ", "    TGGGGGT    ", "    GCCCCCG    ", "    GC#P#CG    ", "    GCP#PCG    ", "    GC#P#CG    ", "    GCCCCCG    ", "    TGGGGGT    ", "               ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "               ", "    TTTTTTT    ", "    TGGGGGT    ", "    TGP#PGT    ", "    TG###GT    ", "    TGP#PGT    ", "    TGGGGGT    ", "    TTTTTTT    ", "               ", "               ", "               ", "               ")
                            .slice("               ", "               ", "               ", "    TTT TTT    ", "   TTPTTTPTT   ", "   TPPGGGPPT   ", "   TTGGGGGTT   ", "    TGGGGGT    ", "   TTGGGGGTT   ", "   TPPGGGPPT   ", "   TTPTTTPTT   ", "    TTT TTT    ", "               ", "               ", "               ")
                            .slice("               ", "               ", "    TTT TTT    ", "    TPT TPT    ", "  TTTT   TTTT  ", "  TPTT   TTPT  ", "  TT       TT  ", "               ", "  TT       TT  ", "  TPTT   TTPT  ", "  TTTT   TTTT  ", "    TPT TPT    ", "    TTT TTT    ", "               ", "               ")
                            .slice("               ", "   TTT   TTT   ", "   TPT   TPT   ", " TTTTT   TTTTT ", " TPTT     TTPT ", " TTT       TTT ", "               ", "               ", "               ", " TTT       TTT ", " TPTT     TTPT ", " TTTTT   TTTTT ", "   TPT   TPT   ", "   TTT   TTT   ", "               ")
                            .slice("               ", "   TTT   TTT   ", "   TPT   TPT   ", " TTTTT   TTTTT ", " TPT       TPT ", " TTT       TTT ", "               ", "               ", "               ", " TTT       TTT ", " TPT       TPT ", " TTTTT   TTTTT ", "   TPT   TPT   ", "   TTT   TTT   ", "               ")
                            .slice("               ", "   TTT   TTT   ", "   TPT   TPT   ", " TTTTT   TTTTT ", " TPT       TPT ", " TTT       TTT ", "               ", "               ", "               ", " TTT       TTT ", " TPT       TPT ", " TTTTT   TTTTT ", "   TPT   TPT   ", "   TTT   TTT   ", "               ")
                            .slice("               ", "   TTT   TTT   ", "  TTPT   TPTT  ", " TTTTT   TTTTT ", " TPTT     TTPT ", " TTT       TTT ", "               ", "               ", "               ", " TTT       TTT ", " TPTT     TTPT ", " TTTTT   TTTTT ", "  TTPT   TPTT  ", "   TTT   TTT   ", "               ")
                            .slice("  LLLLL LLLLL  ", " LLLLLL LLLLLL ", "LLLLPLL LLPLLLL", "LLLLLLL LLLLLLL", "LLPLLLL LLLLPLL", "LLLLLL   LLLLLL", "LLLLL     LLLLL", "               ", "LLLLL     LLLLL", "LLLLLL   LLLLLL", "LLPLLLL LLLLPLL", "LLLLLLL LLLLLLL", "LLLLPLL LLPLLLL", " LLLLLL LLLLLL ", "  LLLLL LLLLL  ")
                            .slice("  LLLLLLLLLLL  ", " LLLLLLLLLLLLL ", "LLLLPLLLLLPLLLL", "LLLLLLLLLLLLLLL", "LLPLLLLLLLLLPLL", "LLLLLLLLLLLLLLL", "LLLLLLLLLLLLLLL", "LLLLLLLLLLLLLLL", "LLLLLLLLLLLLLLL", "LLLLLLLLLLLLLLL", "LLPLLLLLLLLLPLL", "LLLLLLLLLLLLLLL", "LLLLPLLLLLPLLLL", " LLLLLLLLLLLLL ", "  LLLLLLLLLLL  ")
                            .slice("  LLLLLLLLLLL  ", " LLLLLLLLLLLLL ", "LLLLPLLLLLPLLLL", "LLLLLLLLLLLLLLL", "LLPLLLLLLLLLPLL", "LLLLLLLLLLLLLLL", "LLLLLLLLLLLLLLL", "LLLLLLLLLLLLLLL", "LLLLLLLLLLLLLLL", "LLLLLLLLLLLLLLL", "LLPLLLLLLLLLPLL", "LLLLLLLLLLLLLLL", "LLLLPLLLLLPLLLL", " LLLLLLLLLLLLL ", "  LLLLLLLLLLL  ")
                            .slice("  LLLLLLLLLLL  ", " LLLLLLLLLLLLL ", "LLLLPLLLLLPLLLL", "LLLLLLLLLLLLLLL", "LLPLLLLLLLLLPLL", "LLLLLLLLLLLLLLL", "LLLLLLLLLLLLLLL", "LLLLLLLLLLLLLLL", "LLLLLLLLLLLLLLL", "LLLLLLLLLLLLLLL", "LLPLLLLLLLLLPLL", "LLLLLLLLLLLLLLL", "LLLLPLLLLLPLLLL", " LLLLLLLLLLLLL ", "  LLLLLLLLLLL  ")
                            .slice("  LLLLL LLLLL  ", " LLLLLL LLLLLL ", "LLLLPLL LLPLLLL", "LLLLLLL LLLLLLL", "LLPLLLL LLLLPLL", "LLLLLL   LLLLLL", "LLLLL     LLLLL", "               ", "LLLLL     LLLLL", "LLLLLL   LLLLLL", "LLPLLLL LLLLPLL", "LLLLLLL LLLLLLL", "LLLLPLL LLPLLLL", " LLLLLL LLLLLL ", "  LLLLL LLLLL  ")
                            .slice("               ", "   LLL   LLL   ", "  LLOL   LOLL  ", " LLLLL   LLLLL ", " LOLL     LLOL ", " LLL       LLL ", "               ", "               ", "               ", " LLL       LLL ", " LOLL     LLOL ", " LLLLL   LLLLL ", "  LLOL   LOLL  ", "   LLL   LLL   ", "               ")
                            .where('E', Predicates.abilities(INPUT_ENERGY))
                            .where('S', Predicates.controller(blocks(definition.get())))
                            .where('I', Predicates.blocks(CASING_HIGH_TEMPERATURE_SMELTING.get()).or(
                                    Predicates.abilities(IMPORT_ITEMS)).or(Predicates.autoAbilities(true, false, true)))
                            .where('L', Predicates.blocks(CASING_VIBRATION_SAFE.get()))
                            .where('O', Predicates.blocks(CASING_VIBRATION_SAFE.get()).or(Predicates.abilities(IMPORT_FLUIDS_1X, EXPORT_ITEMS, EXPORT_FLUIDS)))
                            .where('T', Predicates.blocks(CASING_HIGH_TEMPERATURE_SMELTING.get()))
                            .where('P', Predicates.blocks(CASING_TUNGSTENSTEEL_PIPE.get()))
                            .where('G', Predicates.blocks(CASING_TEMPERED_GLASS.get()))
                            .where('C', Predicates.heatingCoils())
                            //.where('?', Predicates.blocks(CASING_COKE_BRICKS.get())) //for testing purposes ONLY. none of this structure uses coke oven bricks
                            .where('#', Predicates.air())
                            .where(' ', Predicates.any())
                            .build())
                    .pattern("charcoal_liquefier", definition -> MultiblockPatternBuilder.start(UP, FRONT, RIGHT)
                            .slice(" HHHHH ", "H#####H", "H#####H", "H#####H", "H#####H", "H#####H", " HHHHH ")
                            .where('H', Predicates.blocks(CASING_WATERTIGHT.get()))
                            .where('#', Predicates.air())
                            .where(' ', Predicates.any())
                            .startOffset(OriginOffset.of(0, 17, 0))
                            .anchorOffset(OriginOffset.of(-3, 0, -3))
                            .build())
                    .allowFlip(false)
                    .workableCasingModel(GTCEu.id("block/casings/gcym/high_temperature_smelting_casing"),
                            GTCEu.id("block/multiblock/gcym/mega_blast_furnace"))
                    .tooltips(Component.translatable("gtceu.machine.pyrolyse_oven.tooltip.1"))
                    .additionalDisplay((controller, syncManager) -> {
                        if (!(controller instanceof LargePyrolyserMachine pyrolyserMachine))
                            return Collections.emptyList();
                        BooleanSyncValue isFormed = syncManager.getOrCreateSyncHandler("isFormed", BooleanSyncValue.class,
                                () -> new BooleanSyncValue(controller::isFormed));
                        IntSyncValue coilTier = syncManager.getOrCreateSyncHandler("coilTier", IntSyncValue.class,
                                () -> new IntSyncValue(pyrolyserMachine::getCoilTier));
                        GenericSyncValue<FluidStack> ingredient = syncManager.getOrCreateSyncHandler(
                                "currentFluidBoostType",
                                GenericSyncValue.class,
                                () -> GenericSyncValue.builder(FluidStack.class)
                                        .getter(pyrolyserMachine::getCurrentFluidBoostType)
                                        .adapter(GTByteBufAdapters.makeAdapter(FluidStack.CODEC))
                                        .build()
                        );

                        List<IWidget> display = new ArrayList<>();

                        display.add(Text.dynamic(() -> Component.translatable("gtceu.multiblock.pyrolyse_oven.speed",
                                        coilTier.getIntValue() == 0 ? 75 : 50 * (coilTier.getIntValue() + 1)))
                                .asWidget().setEnabledIf(w -> isFormed.getBoolValue()));
                        if (!ingredient.getValue().equals(FluidStack.EMPTY)) {
                            display.add(Text.dynamic(() -> Component.translatable("oillessgt.multiblock.large_pyrolyse_oven.fluid_boost",
                                    ingredient.getValue().getAmount(), ingredient.getValue().getDisplayName())).asWidget());
                        }

                        return display;
                    })
                    .register();
        }
    }

    public static void initLang(RegistrateLangProvider provider) {
        provider.add("oillessgt.multiblock.large_pyrolyse_oven.fluid_boost", "Currently consuming %d mB/t of %s");
    }
}
