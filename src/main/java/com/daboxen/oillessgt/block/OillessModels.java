package com.daboxen.oillessgt.block;

import com.gregtechceu.gtceu.GTCEu;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

import static com.gregtechceu.gtceu.common.data.models.GTModels.CUBE_ALL_EMISSIVE;

public class OillessModels {

    /**
     * Much of this code was taken from {@link com.gregtechceu.gtceu.common.data.models.GTModels#lampModel}.
     * 
     * @param color  The color of the lamp casing.
     * @param border Whether the lamp has a border.
     * @return A corresponding block model :D
     */
    public static NonNullBiConsumer<DataGenContext<Block, LampCasingBlock>, RegistrateBlockstateProvider> lampCasingModel(DyeColor color,
                                                                                                                          boolean border) {
        return (ctx, prov) -> {
            final String textureBase = "block/lamps/" + color.getSerializedName() + (border ? "" : "_borderless");

            var onModel = prov.models().withExistingParent(ctx.getName() + "_on", CUBE_ALL_EMISSIVE)
                    .texture("all", GTCEu.id(textureBase + "_on"));
            var offModel = prov.models().cubeAll(ctx.getName() + "_off", GTCEu.id(textureBase + "_off"));

            var bloomModel = prov.models().withExistingParent(ctx.getName() + "_bloom",
                    border ? GTCEu.id("block/cube_2_layer/all") : CUBE_ALL_EMISSIVE);
            if (border) {
                bloomModel.texture("bot_all", "gtceu:" + textureBase + "_on");
                bloomModel.texture("top_all", "gtceu:" + textureBase + "_bloom");
            } else {
                bloomModel.texture("all", "gtceu:" + textureBase + "_bloom");
            }

            prov.getVariantBuilder(ctx.getEntry())
                    // spotless:off
                    .partialState()
                    .with(LampCasingBlock.ACTIVE, false)
                    .modelForState().modelFile(offModel)
                    .addModel()
                    .partialState()
                    .with(LampCasingBlock.ACTIVE, true)
                    .with(LampCasingBlock.BLOOM, false)
                    .modelForState().modelFile(onModel)
                    .addModel()
                    .partialState()
                    .with(LampCasingBlock.ACTIVE, true)
                    .with(LampCasingBlock.BLOOM, true)
                    .modelForState().modelFile(bloomModel)
                    .addModel();
            // spotless:on
        };
    }
}
