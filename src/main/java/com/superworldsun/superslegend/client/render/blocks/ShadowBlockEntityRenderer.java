package com.superworldsun.superslegend.client.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.superworldsun.superslegend.blocks.entity.ShadowBlockEntity;
import com.superworldsun.superslegend.registries.BlockInit;
import com.superworldsun.superslegend.registries.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.model.data.ModelData;

public class ShadowBlockEntityRenderer<T extends ShadowBlockEntity> implements BlockEntityRenderer<T> {
    protected final Minecraft minecraft;
    protected final BlockRenderDispatcher blockRenderer;
    protected final BlockColors blockColors;

    public ShadowBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.minecraft = Minecraft.getInstance();
        this.blockRenderer = minecraft.getBlockRenderer();
        this.blockColors = minecraft.getBlockColors();
    }

    @Override
    public void render(T blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {


        BlockState state = blockEntity.getAppearance();
        Level level = blockEntity.getLevel();
        BlockPos pos = blockEntity.getBlockPos();

        if (level == null) return;
        boolean clientPlayerUsingLens = minecraft.player.isUsingItem() && minecraft.player.getUseItem().getItem() == ItemInit.LENS_OF_TRUTH.get();
        if (clientPlayerUsingLens) {
            state = BlockInit.SHADOW_MODEL_BLOCK.get().defaultBlockState();
        }

        renderBlock(state, poseStack, bufferSource, blockEntity.getModelData(), level, pos, packedLight);
    }

    protected void renderBlock(BlockState pState,  PoseStack pPoseStack, MultiBufferSource pBufferSource,
                              ModelData pModelData, Level pLevel, BlockPos pPos, int pPackedLight) {

        RenderType renderType = ItemBlockRenderTypes.getChunkRenderType(pState);

        VertexConsumer vertexConsumer = pBufferSource.getBuffer(RenderTypeHelper.getEntityRenderType(renderType, true));
        BakedModel model = blockRenderer.getBlockModel(pState);
        pPoseStack.pushPose();
        PoseStack.Pose lastPose = pPoseStack.last();

        int tintColor = blockColors.getColor(pState, pLevel, pPos, 0);
        float red = (tintColor >> 16 & 255) / 255.0F;
        float green = (tintColor >> 8 & 255) / 255.0F;
        float blue = (tintColor & 255) / 255.0F;

        blockRenderer.getModelRenderer().renderModel(
                lastPose,
                vertexConsumer,
                pState,
                model,
                red,
                green,
                blue,
                pPackedLight,
                OverlayTexture.NO_OVERLAY,
                pModelData,
                renderType
        );
        pPoseStack.popPose();
    }
}
