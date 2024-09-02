package com.superworldsun.superslegend.client.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.superworldsun.superslegend.blocks.entity.HiddenShadowBlockEntity;
import com.superworldsun.superslegend.registries.ItemInit;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HiddenShadowBlockEntityRenderer extends ShadowBlockEntityRenderer<HiddenShadowBlockEntity> {
    public HiddenShadowBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(HiddenShadowBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        BlockState state = blockEntity.getAppearance();
        Level level = blockEntity.getLevel();
        BlockPos pos = blockEntity.getBlockPos();

        if (level == null) return;
        if (!minecraft.player.isUsingItem() || minecraft.player.getUseItem().getItem() != ItemInit.LENS_OF_TRUTH.get()) {
            return;
        }
        renderBlock(state, poseStack, bufferSource, blockEntity.getModelData(), level, pos, packedLight);

    }
}
