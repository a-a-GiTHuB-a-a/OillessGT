package com.daboxen.oillessgt.block;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.api.block.property.GTBlockStateProperties;
import com.gregtechceu.gtceu.common.block.LampBlock;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.HitResult;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Similar to a {@link LampBlock}, but meant to be used inside a machine.
 * Instead of toggling with a redstone signal, it toggles with multiblock activity.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LampCasingBlock extends ActiveBlock {

    public static final BooleanProperty BLOOM = BlockStateProperties.BLOOM;
    public static final BooleanProperty LIGHT = BlockStateProperties.LIT;
    public static final BooleanProperty ACTIVE = GTBlockStateProperties.ACTIVE;

    public static final String TAG_BLOOM = "bloom";
    public static final String TAG_LIGHT = "lit";

    public static final int BLOOM_FLAG = 0b001;
    public static final int LIGHT_FLAG = 0b010;

    public final DyeColor color;
    public final boolean bordered;

    public LampCasingBlock(Properties properties, DyeColor color, boolean bordered) {
        super(properties);
        this.color = color;
        this.bordered = bordered;
        registerDefaultState(defaultBlockState()
                .setValue(BLOOM, true)
                .setValue(LIGHT, true)
                .setValue(ACTIVE, false));
    }

    public static boolean isLightActive(BlockState state) {
        return state.getValue(ACTIVE);
    }

    public static boolean isLightEnabled(CompoundTag tag) {
        return tag.getBoolean(TAG_LIGHT);
    }

    public static boolean isBloomEnabled(CompoundTag tag) {
        return tag.getBoolean(TAG_BLOOM);
    }

    public CompoundTag getTagFromState(BlockState state) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(TAG_BLOOM, state.getValue(BLOOM));
        tag.putBoolean(TAG_LIGHT, state.getValue(LIGHT));
        return tag;
    }

    public ItemStack getStackFromIndex(int i) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(LampBlock.TAG_BLOOM, (i & BLOOM_FLAG) != 0);
        tag.putBoolean(LampBlock.TAG_LIGHT, (i & LIGHT_FLAG) != 0);
        ItemStack stack = new ItemStack(this);
        stack.setTag(tag);
        return stack;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BLOOM, LIGHT, ACTIVE);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(LIGHT) && isLightActive(state) ? 15 : 0;
    }

    @Override
    public BlockState getAppearance(BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side,
                                    @Nullable BlockState queryState, @Nullable BlockPos queryPos) {
        return state.getBlock().defaultBlockState();
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target,
                                       BlockGetter level, BlockPos pos, Player player) {
        ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);
        stack.setTag(getTagFromState(state));
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip,
                                TooltipFlag flag) {
        if (stack.hasTag()) {
            var tag = stack.getTag();

            if (!isBloomEnabled(tag)) tooltip.add(Component.translatable("block.gtceu.lamp.tooltip.no_bloom"));
            if (!isLightEnabled(tag)) tooltip.add(Component.translatable("block.gtceu.lamp.tooltip.no_light"));
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> returnValue = super.getDrops(state, params);
        for (ItemStack stack : returnValue) {
            if (stack.is(this.asItem())) {
                stack.setTag(this.getTagFromState(state));
                break;
            }
        }
        return returnValue;
    }
}
