package com.superworldsun.superslegend.client.render.seeds;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.superworldsun.superslegend.entities.projectiles.seeds.SeedEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class SeedRenderer<T extends SeedEntity> extends EntityRenderer<T> {
    private final ResourceLocation texture;
    private final RenderType renderType;

    public SeedRenderer(EntityRendererProvider.Context ctx, ResourceLocation texture) {
        super(ctx);
        this.texture = texture;
        this.renderType = RenderType.entityCutoutNoCull(texture);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return texture;
    }
    
    @Override
    public void render(@NotNull T entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        float scale = 0.5f;
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180f));
        PoseStack.Pose poseStack$entry = poseStack.last();
        Matrix4f pose = poseStack$entry.pose();
        Matrix3f normal = poseStack$entry.normal();
        VertexConsumer vertexBuilder = bufferSource.getBuffer(renderType);
        vertex(vertexBuilder, pose, normal, packedLight, 0f, 0f, 0f, 1f);
        vertex(vertexBuilder, pose, normal, packedLight, 1f, 0f, 1f, 1f);
        vertex(vertexBuilder, pose, normal, packedLight, 1f, 1f, 1f, 0f);
        vertex(vertexBuilder, pose, normal, packedLight, 0f, 1f, 0f, 0f);
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    private static void vertex(VertexConsumer vertexBuilder, Matrix4f pose, Matrix3f normal, int light, float x, float y, float u, float v) {
        vertexBuilder.vertex(pose, x - 0.5f, y - 0.25f, 0f)
                .color(255, 255, 255, 255)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 0f, 1f, 0f)
                .endVertex();
    }
}