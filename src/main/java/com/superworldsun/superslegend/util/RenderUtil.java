package com.superworldsun.superslegend.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;

/**
 * Provides some useful rendering functions.
 */
public final class RenderUtil {

    public static void renderProjectileEntityWithModulation(ThrowableItemProjectile entity,
                                                      PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                                      EntityRenderDispatcher renderManager, ItemRenderer itemRenderer,
                                                      float red, float green, float blue) {

        if (entity.tickCount < 2 && renderManager.camera.getEntity().distanceToSqr(entity) < 12.25D)
            return;
        poseStack.pushPose();

        poseStack.scale(1.0F, 1.0F, 1.0F);
        poseStack.mulPose(renderManager.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        int combinedOverlayIn = OverlayTexture.NO_OVERLAY;

        ItemStack itemStack = entity.getItem();
        BakedModel bakedModel = itemRenderer.getModel(itemStack, entity.level(), null, entity.getId());

        poseStack.translate(-0.5D, -0.2D, -0.5D);
        RenderType renderType = ItemBlockRenderTypes.getRenderType(itemStack, false);

        VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
        renderQuadsWithModulation(poseStack, vertexConsumer, bakedModel, packedLight, combinedOverlayIn, red, green, blue);

        poseStack.popPose();
    }

    private static void renderQuadsWithModulation(PoseStack poseStack, VertexConsumer vertexConsumer,
                                                  BakedModel bakedModel, int packedLight, int packedOverlay, float red, float green, float blue) {
        RandomSource random = RandomSource.create();
        for (BakedQuad quad : bakedModel.getQuads(null, null, random, null, null)) {
            vertexConsumer.putBulkData(poseStack.last(), quad, red, green, blue, 1.0F, packedLight, packedOverlay, true);
        }
    }
}

