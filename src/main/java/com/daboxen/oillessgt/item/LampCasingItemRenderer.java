package com.daboxen.oillessgt.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.NotNull;

/**
 * All this renderer does is refer rendering to the correct block model based on the lamp item's NBT.<br>
 * Without it, all item variants would look like the default 'lit, with bloom' one.
 */
public class LampCasingItemRenderer extends BlockEntityWithoutLevelRenderer {

    private static LampCasingItemRenderer INSTANCE = null;

    public static LampCasingItemRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LampCasingItemRenderer();
        }
        return INSTANCE;
    }

    protected final ItemRenderer itemRenderer;
    protected final BlockRenderDispatcher blockRenderer;

    protected LampCasingItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext,
                             @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer,
                             int packedLight, int packedOverlay) {
        if (!(stack.getItem() instanceof LampCasingBlockItem item)) {
            return;
        }
        BlockState state = item.getStateFromStack(stack, item.getBlock().defaultBlockState());
        BakedModel p_model = blockRenderer.getBlockModel(state);

        for (var model : p_model.getRenderPasses(stack, true)) {
            for (var rendertype : model.getRenderTypes(stack, true)) {
                VertexConsumer foilBuffer = ItemRenderer.getFoilBufferDirect(buffer, rendertype, true, stack.hasFoil());
                itemRenderer.renderModelLists(model, stack, packedLight, packedOverlay, poseStack, foilBuffer);
            }
        }
    }
}
