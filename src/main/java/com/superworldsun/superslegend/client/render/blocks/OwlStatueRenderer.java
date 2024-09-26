package com.superworldsun.superslegend.client.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.blocks.entity.OwlStatueBlockEntity;
import com.superworldsun.superslegend.capability.waypoint.WaypointsProvider;
import com.superworldsun.superslegend.client.model.ModelLayers;
import com.superworldsun.superslegend.client.model.OpenOwlStatueModel;
import com.superworldsun.superslegend.client.model.OwlStatueModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.concurrent.atomic.AtomicBoolean;

@OnlyIn(Dist.CLIENT)
public class OwlStatueRenderer implements BlockEntityRenderer<OwlStatueBlockEntity> {
    private static final ResourceLocation TEXTURE_CLOSED = new ResourceLocation(SupersLegendMain.MOD_ID, "textures/models/block/owl_statue.png");
    private static final ResourceLocation TEXTURE_OPEN = new ResourceLocation(SupersLegendMain.MOD_ID, "textures/models/block/owl_statue_open.png");
    private final OwlStatueModel closedStatueModel;
    private final OpenOwlStatueModel openStatueModel;

    public OwlStatueRenderer(BlockEntityRendererProvider.Context context) {
        closedStatueModel = new OwlStatueModel(context.bakeLayer(ModelLayers.OWL_STATUE_CLOSED));
        openStatueModel = new OpenOwlStatueModel(context.bakeLayer(ModelLayers.OWL_STATUE_OPEN));
    }

    @Override
    public void render(OwlStatueBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Minecraft client = Minecraft.getInstance();
        poseStack.pushPose();
        Direction blockFacing = blockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.mulPose(Axis.YP.rotationDegrees(blockFacing.toYRot()));

        AtomicBoolean waypointSaved = new AtomicBoolean(false);
        BlockPos waypointPos = blockEntity.getBlockPos().relative(blockFacing);

        client.player.getCapability(WaypointsProvider.WAYPOINTS_CAPABILITY).ifPresent(waypoints -> {
            if (waypoints.getWaypoint(waypointPos) != null) {
                waypointSaved.set(true);
            }
        });

        ResourceLocation texture = waypointSaved.get() ? TEXTURE_OPEN : TEXTURE_CLOSED;
        Model model = waypointSaved.get() ? openStatueModel : closedStatueModel;

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        model.renderToBuffer(poseStack, vertexConsumer, combinedLight, combinedOverlay, 1F, 1F, 1F, 1F);

        poseStack.popPose();
    }
}