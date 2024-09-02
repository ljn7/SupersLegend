package com.superworldsun.superslegend.client.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.superworldsun.superslegend.blocks.entity.FalseShadowBlockEntity;
import com.superworldsun.superslegend.registries.ItemInit;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class FalseShadowBlockEntityRenderer extends ShadowBlockEntityRenderer<FalseShadowBlockEntity> {
    public FalseShadowBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(FalseShadowBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        // Do not render if player is not using lens
        if (minecraft.player.isUsingItem() && minecraft.player.getUseItem().getItem() == ItemInit.LENS_OF_TRUTH.get()) {
            return;
        }
        super.render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }
}
