package com.superworldsun.superslegend.client.render.bewlr;

import com.mojang.blaze3d.vertex.PoseStack;
import com.superworldsun.superslegend.items.block.ShadowBlockItem;
import com.superworldsun.superslegend.registries.BlockInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ShadowBlockItemRenderer  extends BlockEntityWithoutLevelRenderer {
    protected final BlockRenderDispatcher blockRenderer;
    protected ItemRenderer itemRenderer;
    public ShadowBlockItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void renderByItem(@NotNull ItemStack itemStack, @NotNull ItemDisplayContext ctx, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        BlockState blockStateToRender = ShadowBlockItem.loadDisguiseFromStack(itemStack);
        Block blockToRender = Optional.ofNullable(blockStateToRender)
                .map(BlockState::getBlock)
                .orElse(BlockInit.SHADOW_MODEL_BLOCK.get());

        poseStack.pushPose();
        boolean hand = ctx == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        poseStack.translate(0.5, 0.5, 0.5);

        BlockState stateForRendering = blockToRender.defaultBlockState();
        BakedModel modelToRender = blockRenderer.getBlockModel(stateForRendering);

        itemRenderer.render(new ItemStack(blockToRender),
                ctx,
                hand,
                poseStack,
                bufferSource,
                combinedLight,
                combinedOverlay,
                modelToRender
        );
        poseStack.popPose();
    }

}
