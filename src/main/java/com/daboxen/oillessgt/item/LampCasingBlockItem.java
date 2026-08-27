package com.daboxen.oillessgt.item;

import com.daboxen.oillessgt.block.LampCasingBlock;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.daboxen.oillessgt.block.LampCasingBlock.isBloomEnabled;
import static com.daboxen.oillessgt.block.LampCasingBlock.isLightEnabled;

/**
 * Much of this code is taken from {@link com.gregtechceu.gtceu.common.item.LampBlockItem LampBlockItem}, except that it
 * corresponds to {@link LampCasingBlock} instead of {@link com.gregtechceu.gtceu.common.block.LampBlock LampBlock}
 */
@ParametersAreNonnullByDefault
public class LampCasingBlockItem extends BlockItem {

    public LampCasingBlockItem(LampCasingBlock block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull LampCasingBlock getBlock() {
        return (LampCasingBlock) super.getBlock();
    }

    @Override
    protected @Nullable BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if (state == null) return null;

        return getStateFromStack(context.getItemInHand(), state);
    }

    public BlockState getStateFromStack(ItemStack stack, BlockState baseState) {
        if (!stack.hasTag() || !stack.is(this)) {
            return baseState;
        }
        var tag = stack.getTag();
        return baseState.setValue(LampCasingBlock.BLOOM, isBloomEnabled(tag))
                .setValue(LampCasingBlock.LIGHT, isLightEnabled(tag));
    }

    public NonNullList<ItemStack> getItemVariants() {
        NonNullList<ItemStack> stacks = NonNullList.createWithCapacity(4);
        for (int i = 0; i < 4; ++i) {
            stacks.add(this.getBlock().getStackFromIndex(i));
        }
        return stacks;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return LampCasingItemRenderer.getInstance();
            }
        });
    }
}
