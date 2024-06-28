// PedestalRenderer.java
package com.superworldsun.superslegend.client.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.superworldsun.superslegend.blocks.PedestalBlock;
import com.superworldsun.superslegend.blocks.entity.PedestalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class PedestalRenderer implements BlockEntityRenderer<PedestalBlockEntity> {

	public PedestalRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(PedestalBlockEntity te, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		ItemStack sword = te.getSword();
		if (!sword.isEmpty()) {
			this.renderItem(te, sword, poseStack, buffer, combinedLight, combinedOverlay);
		}
	}

	private void rotateItem(PoseStack poseStack, float x, float y, float z) {
		poseStack.mulPose(Axis.XP.rotationDegrees(x));
		poseStack.mulPose(Axis.YP.rotationDegrees(y));
		poseStack.mulPose(Axis.ZP.rotationDegrees(z));
	}

	private void renderItem(PedestalBlockEntity te, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		poseStack.pushPose();
		poseStack.translate(0.5D, 0.6D, 0.5D);

		BlockState state = te.getBlockState();
		if (state.getBlock() instanceof PedestalBlock) {
			switch (state.getValue(PedestalBlock.FACING)) {
				case WEST:
				case EAST:
					this.rotateItem(poseStack, 180f, 90f, -45f);
					break;
				case NORTH:
				case SOUTH:
					this.rotateItem(poseStack, 180f, 180f, -45f);
					break;
				default:
					break;
			}
		}

		Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffer, te.getLevel(), combinedLight);
		poseStack.popPose();
	}
}
