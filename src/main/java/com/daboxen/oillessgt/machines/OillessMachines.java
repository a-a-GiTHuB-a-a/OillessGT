package com.daboxen.oillessgt.machines;

import com.daboxen.oillessgt.config.OillessConfiguration;
import com.daboxen.oillessgt.machines.multiblock.GreenhouseMachine;
import com.daboxen.oillessgt.machines.multiblock.LargePyrolyserMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.multiblock.OriginOffset;
import com.gregtechceu.gtceu.api.multiblock.Predicates;
import com.gregtechceu.gtceu.api.multiblock.pattern.MultiblockPatternBuilder;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.common.data.GTMaterialBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterialItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fluids.FluidStack;

import brachy.modularui.api.drawable.Text;
import brachy.modularui.api.widget.IWidget;
import brachy.modularui.value.sync.BooleanSyncValue;
import brachy.modularui.value.sync.IntSyncValue;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.daboxen.oillessgt.OillessGTMod.OILLESS_LOGGER;
import static com.daboxen.oillessgt.OillessGTMod.OILLESS_REGISTRATE;
import static com.daboxen.oillessgt.block.OillessBlocks.GROWTH_LIGHT;
import static com.daboxen.oillessgt.block.OillessBlocks.REINFORCED_DIRT;
import static com.daboxen.oillessgt.recipe.OillessRecipeTypes.GREENHOUSE_RECIPES;
import static com.daboxen.oillessgt.recipe.OillessRecipeTypes.LARGE_PYROLYSE_RECIPES;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.multiblock.Predicates.blocks;
import static com.gregtechceu.gtceu.api.multiblock.util.RelativeDirection.*;
import static com.gregtechceu.gtceu.common.data.GCYMBlocks.CASING_HIGH_TEMPERATURE_SMELTING;
import static com.gregtechceu.gtceu.common.data.GCYMBlocks.CASING_VIBRATION_SAFE;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.BATCH_MODE;
import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.ELECTRIC_OVERCLOCK;

public class OillessMachines {

    public static @Nullable MultiblockMachineDefinition LARGE_PYROLYSER;
    public static @Nullable MultiblockMachineDefinition GREENHOUSE;

    public static void init(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
        OILLESS_LOGGER.info("Registering machines…");

        if (OillessConfiguration.INSTANCE.addLargePyrolyser) {
            LARGE_PYROLYSER = OILLESS_REGISTRATE
                    .multiblock("large_pyrolyser", LargePyrolyserMachine::new)
                    .langValue("Trunk-Ravaging Energized Extractor")
                    .rotationState(RotationState.NON_Y_AXIS)
                    .recipeType(LARGE_PYROLYSE_RECIPES)
                    .recipeModifiers(LargePyrolyserMachine::recipeModifier, GTRecipeModifiers::hatchParallel,
                            GTRecipeModifiers::pyrolyseOvenOverclock, BATCH_MODE)
                    .appearanceBlock(CASING_HIGH_TEMPERATURE_SMELTING)
                    .partAppearance((controller, part, side) -> {
                        int NEAR_BOUGHS = 10; // number chosen somewhat arbitrarily—should work
                        int partHeight = part.getBlockPos().getY();
                        int controllerHeight = controller.getBlockPos().getY();
                        if ((partHeight - controllerHeight) > NEAR_BOUGHS) {
                            // OILLESS_LOGGER.info("bough found!!! Position: {}", part.getBlockPos().toString());
                            return CASING_VIBRATION_SAFE.getDefaultState();
                        }
                        return CASING_HIGH_TEMPERATURE_SMELTING.getDefaultState();
                    })
                    .pattern(definition -> MultiblockPatternBuilder.start(UP, FRONT, RIGHT)
                            // spotless:off
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
                            //spotless:on
                            .where('E', Predicates.abilities(INPUT_ENERGY))
                            .where('S', Predicates.controller(blocks(definition.get())))
                            .where('I', Predicates.blocks(CASING_HIGH_TEMPERATURE_SMELTING.get()).or(
                                    Predicates.abilities(IMPORT_ITEMS)).or(Predicates.autoAbilities(true, false, true)))
                            .where('L', Predicates.blocks(CASING_VIBRATION_SAFE.get()))
                            .where('O',
                                    Predicates.blocks(CASING_VIBRATION_SAFE.get())
                                            .or(Predicates.abilities(IMPORT_FLUIDS_1X, EXPORT_ITEMS, EXPORT_FLUIDS))
                                            .or(Predicates.abilities(MUFFLER).setExactLimit(1)))
                            .where('T', Predicates.blocks(CASING_HIGH_TEMPERATURE_SMELTING.get()))
                            .where('P', Predicates.blocks(CASING_TUNGSTENSTEEL_PIPE.get()))
                            .where('G', Predicates.blocks(CASING_TEMPERED_GLASS.get()))
                            .where('C', Predicates.heatingCoils())
                            // .where('?', Predicates.blocks(CASING_COKE_BRICKS.get())) //for testing purposes ONLY.
                            // none of this structure uses coke oven bricks
                            .where('#', Predicates.air())
                            .where(' ', Predicates.any())
                            .build())
                    /*
                     * .pattern("charcoal_liquefier", definition -> MultiblockPatternBuilder.start(UP, FRONT, RIGHT)
                     * .slice(" HHHHH ", "H#####H", "H#####H", "H#####H", "H#####H", "H#####H", " HHHHH ")
                     * .where('H', Predicates.blocks(CASING_WATERTIGHT.get()))
                     * .where('#', Predicates.air())
                     * .where(' ', Predicates.any())
                     * .startOffset(OriginOffset.of(0, 17, 0))
                     * .anchorOffset(OriginOffset.of(-3, 0, -3))
                     * .build())
                     */ // im just doing this in its own recipe lowk
                    .recoveryItems(
                            () -> new ItemLike[] {
                                    GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.dust, GTMaterials.Ash).get() })
                    .allowFlip(false)
                    .workableCasingModel(GTCEu.id("block/casings/gcym/high_temperature_smelting_casing"),
                            GTCEu.id("block/multiblock/gcym/mega_blast_furnace"))
                    .tooltips(Component.translatable("gtceu.machine.pyrolyse_oven.tooltip.1"))
                    .additionalDisplay((controller, syncManager) -> {
                        if (!(controller instanceof LargePyrolyserMachine pyrolyserMachine))
                            return Collections.emptyList();
                        BooleanSyncValue isFormed = syncManager.getOrCreateSyncHandler("isFormed",
                                BooleanSyncValue.class,
                                () -> new BooleanSyncValue(controller::isFormed));
                        IntSyncValue coilTier = syncManager.getOrCreateSyncHandler("coilTier", IntSyncValue.class,
                                () -> new IntSyncValue(pyrolyserMachine::getCoilTier));
                        /*
                         * GenericSyncValue<FluidStack> ingredient = syncManager.getOrCreateSyncHandler(
                         * "currentFluidBoostType",
                         * GenericSyncValue.class,
                         * () -> GenericSyncValue.builder(FluidStack.class)
                         * .getter(pyrolyserMachine::getCurrentFluidBoostType)
                         * .adapter(GTByteBufAdapters.makeAdapter(FluidStack.CODEC))
                         * .build());
                         */
                        IntSyncValue gasTier = syncManager.getOrCreateSyncHandler("currentGasTier", IntSyncValue.class,
                                () -> new IntSyncValue(pyrolyserMachine::getCurrentGasTier));
                        FluidStack ingredient = LargePyrolyserMachine.getBoostFluid(gasTier.getIntValue())
                                .getStacks()[0];

                        List<IWidget> display = new ArrayList<>();

                        display.add(Text.dynamic(() -> Component.translatable("gtceu.multiblock.pyrolyse_oven.speed",
                                coilTier.getIntValue() == 0 ? 75 : 50 * (coilTier.getIntValue() + 1)))
                                .asWidget().setEnabledIf(w -> isFormed.getBoolValue()));
                        if (pyrolyserMachine.isActive() && !ingredient.equals(FluidStack.EMPTY)) {
                            display.add(Text
                                    .dynamic(() -> Component.translatable(
                                            "oillessgt.multiblock.large_pyrolyse_oven.fluid_boost",
                                            ingredient.getAmount(), ingredient.getDisplayName()))
                                    .asWidget());
                        }

                        return display;
                    })
                    .register();
        }

        if (OillessConfiguration.INSTANCE.addGreenhouse) {
            GREENHOUSE = OILLESS_REGISTRATE
                    .multiblock("greenhouse", GreenhouseMachine::new)
                    .langValue("Plant Growth Chamber")
                    .rotationState(RotationState.NON_Y_AXIS)
                    .recipeType(GREENHOUSE_RECIPES)
                    .recipeModifiers(
                            ELECTRIC_OVERCLOCK.apply(
                                    OverclockingLogic.create(1 / 3.0, OverclockingLogic.STD_VOLTAGE_FACTOR, true)),
                            BATCH_MODE)
                    .appearanceBlock(CASING_STEEL_SOLID)
                    // TODO: somehow gate certain recipes behind internal substructure. (I HATE SUBSTRUCTURES)
                    .pattern(definition -> MultiblockPatternBuilder.start(UP, FRONT, RIGHT)
                            // spotless:off
                            .slice("CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC")
                            .slice("CCCCCCCCC", "C       C", "C       C", "C       C", "C       C", "C       C", "C       C", "C       C", "CCCCSCCCC")
                            .slice("CGGGGGGGC", "G       G", "G       G", "G       G", "G       G", "G       G", "G       G", "G       G", "CGGGGGGGC")
                            .slice("CGGGGGGGC", "G       G", "G       G", "G       G", "G       G", "G       G", "G       G", "G       G", "CGGGGGGGC")
                            .slice("CGGGGGGGC", "G       G", "G       G", "G       G", "G       G", "G       G", "G       G", "G       G", "CGGGGGGGC")
                            .slice("CGGGGGGGC", "G       G", "G       G", "G       G", "G       G", "G       G", "G       G", "G       G", "CGGGGGGGC")
                            .slice("CGGGGGGGC", "G       G", "G       G", "G       G", "G       G", "G       G", "G       G", "G       G", "CGGGGGGGC")
                            .slice("CGGGGGGGC", "G       G", "G       G", "G       G", "G       G", "G       G", "G       G", "G       G", "CGGGGGGGC")
                            .slice("CCCCCCCCC", "CGGGGGGGC", "CGGGGGGGC", "CGGCCCGGC", "CGGCCCGGC", "CGGCCCGGC", "CGGGGGGGC", "CGGGGGGGC", "CCCCCCCCC")
                            // spotless:on
                            .where('S', Predicates.controller(definition))
                            .where('C', Predicates.blocks(CASING_STEEL_SOLID.get())
                                    .or(Predicates.autoAbilities(GREENHOUSE_RECIPES)))
                            .where('G', Predicates.blocks(CASING_TEMPERED_GLASS.get()))
                            .where('#', Predicates.air())
                            .where(' ', Predicates.any())
                            .build())
                    .pattern("arboretum", definition -> MultiblockPatternBuilder.start(UP, FRONT, RIGHT)
                            // spotless:off
                            .slice("DDPXPDD", "DDDDDDD", "PDDDDDP", "XDDDDDX", "PDDDDDP", "DDDDDDD", "DDPXPDD")
                            .slice("##PXP##", "#######", "P#####P", "X##T##X", "P#####P", "#######", "##PXP##")
                            .slice("##PXP##", "#######", "P#####P", "X##T##X", "P#####P", "#######", "##PXP##")
                            .slice("##PXP##", "#######", "P##T##P", "X#TTT#X", "P##T##P", "#######", "##PXP##")
                            .slice("##PXP##", "#######", "P#####P", "X##T##X", "P#####P", "#######", "##PXP##")
                            .slice("##PXP##", "#######", "P#####P", "X#####X", "P#####P", "#######", "##PXP##")
                            .slice("##PXP##", "#######", "P#MMM#P", "X#MMM#X", "P#MMM#P", "#######", "##PXP##")
                            // spotless:on
                            .where('P', Predicates.blocks(CASING_STEEL_PIPE.get()))
                            .where('X',
                                    Predicates.blocks(GTMaterialBlocks.MATERIAL_BLOCKS
                                            .get(TagPrefix.frameGt, GTMaterials.BlackSteel).get()))
                            .where('D', Predicates.blocks(REINFORCED_DIRT.get()))
                            .where('T',
                                    Predicates.blocks(GTMaterialBlocks.MATERIAL_BLOCKS
                                            .get(TagPrefix.frameGt, GTMaterials.TreatedWood).get()))
                            .where('M', Predicates.blocks(GROWTH_LIGHT.get()))
                            .where('#', Predicates.air())
                            .where(' ', Predicates.any())
                            .anchorOffset(OriginOffset.of(RIGHT, 3).move(FRONT, 3))
                            .startOffset(OriginOffset.of(BACK, 4))
                            .build())
                    .allowFlip(false)
                    .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_solid_steel"),
                            GTCEu.id("block/multiblock/implosion_compressor"))
                    .tooltips(Component.translatable("oillessgt.multiblock.greenhouse.tooltip.1"),
                            Component.translatable("oillessgt.multiblock.greenhouse.tooltip.2"))
                    .additionalDisplay((controller, syncManager) -> {
                        if (!(controller instanceof GreenhouseMachine greenhouse)) {
                            return List.of();
                        }
                        return List.of(
                                Text.dynamic(
                                        () -> Component.translatable("oillessgt.multiblock.greenhouse.substructure",
                                                greenhouse.getInternalStructureName()))
                                        .asWidget());
                    })
                    .register();
        }
    }

    public static void initLang(RegistrateLangProvider provider) {
        provider.add("oillessgt.multiblock.large_pyrolyse_oven.fluid_boost", "Currently consuming %d mB/t of %s");
        provider.add("oillessgt.multiblock.large_pyrolyse_oven.low_gas_tier", "Current inert gas tier too low.");
        provider.add("oillessgt.multiblock.greenhouse.tooltip.1", "Now with extra efficiency!");
        provider.add("oillessgt.multiblock.greenhouse.tooltip.2",
                "Has 3/4 overclocks and subticking; for every overclock, it gains a 3x speed boost instead of a 4x speed boost.");
        provider.add("oillessgt.multiblock.greenhouse.substructure", "Currently has internal structure '%s'");
    }
}
