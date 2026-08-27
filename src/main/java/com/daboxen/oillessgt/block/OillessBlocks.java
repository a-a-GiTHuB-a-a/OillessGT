package com.daboxen.oillessgt.block;

import com.daboxen.oillessgt.OillessGTMod;
import com.daboxen.oillessgt.item.LampCasingBlockItem;

import com.gregtechceu.gtceu.client.model.item.CustomItemRendererWrapperModel;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import com.tterrag.registrate.util.entry.BlockEntry;

import static com.daboxen.oillessgt.OillessGTMod.OILLESS_LOGGER;
import static com.daboxen.oillessgt.OillessGTMod.OILLESS_REGISTRATE;

public class OillessBlocks {

    public static final BlockEntry<Block> REINFORCED_DIRT = OILLESS_REGISTRATE
            .block("reinforced_dirt", Block::new)
            .lang("Reinforced Dirt")
            .initialProperties(() -> Blocks.DIRT)
            .properties(p -> p.requiresCorrectToolForDrops())
            .tag(BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.NEEDS_STONE_TOOL, BlockTags.DIRT)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(), prov.models().cubeBottomTop(ctx.getName(),
                    OillessGTMod.id("block/reinforced_dirt/side"),
                    OillessGTMod.id("block/reinforced_dirt/bottom"),
                    OillessGTMod.id("block/reinforced_dirt/top"))))
            .item(BlockItem::new)
            .tab(GTCreativeModeTabs.DECORATION.getKey())
            .build()
            .register();

    public static final BlockEntry<LampCasingBlock> GROWTH_LIGHT = OILLESS_REGISTRATE
            .block("growth_light", data -> new LampCasingBlock(data, DyeColor.MAGENTA, false))
            .lang("Growth Light")
            .initialProperties(() -> GTBlocks.BORDERLESS_LAMPS.get(DyeColor.MAGENTA).get())
            .addLayer(() -> RenderType::cutout)
            .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
            .blockstate(OillessModels.lampCasingModel(DyeColor.MAGENTA, true))
            .item(LampCasingBlockItem::new)
            .model((ctx, prov) -> prov.blockItem(ctx::get, "_on")
                    .customLoader(CustomItemRendererWrapperModel.Builder::begin).end())
            .removeTab(CreativeModeTabs.SEARCH)
            .tab(GTCreativeModeTabs.DECORATION.getKey(), (cxt, mod) -> mod.acceptAll(cxt.getEntry().getItemVariants()))
            .build()
            .register();

    public static void init() {
        OILLESS_LOGGER.info("Registering blocks…");
    }
}
