package com.superworldsun.superslegend.client.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.blocks.entity.FanBlockEntity;
import com.superworldsun.superslegend.client.model.FanModel;
import com.superworldsun.superslegend.client.model.ModelLayers;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class FanRenderer implements BlockEntityRenderer<FanBlockEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SupersLegendMain.MOD_ID, "textures/models/block/fan.png");
    private final FanModel model;

    public FanRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new FanModel(context.bakeLayer(ModelLayers.FAN));
    }

    @Override
    public void render(FanBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (blockEntity.isPowered()) {
            blockEntity.bladesRotation -= partialTick;
        }

        poseStack.pushPose();
        Direction fanDirection = blockEntity.getFanDirection();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        boolean flipRotation = fanDirection == Direction.SOUTH || fanDirection == Direction.NORTH;
        poseStack.mulPose(Axis.YP.rotationDegrees(fanDirection.toYRot() + (flipRotation ? 180 : 0)));
        poseStack.translate(-0.5D, -0.5D, -0.5D);

        this.model.render(poseStack, buffer.getBuffer(model.renderType(TEXTURE)),
                packedLight, packedOverlay, partialTick, blockEntity.bladesRotation);

        poseStack.popPose();
    }
}