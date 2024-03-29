package com.superworldsun.superslegend.client.render.boomerang;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.superworldsun.superslegend.entities.projectiles.boomerang.AbstractBoomerangEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BoomerangRenderer extends EntityRenderer<AbstractBoomerangEntity> {
    private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
    private final Item iconItem;

    public BoomerangRenderer(EntityRendererProvider.Context renderManager, Item iconItem) {
        super(renderManager);
        this.iconItem = iconItem;
    }

    @Override
    public void render(AbstractBoomerangEntity entity, float yaw, float partialTicks, PoseStack poseStack, @NotNull MultiBufferSource buffer, int light) {
        poseStack.pushPose();
        poseStack.translate(0f, entity.getBbHeight() / 2f, 0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-yaw + 90f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.xRotO) + 90f));
        poseStack.mulPose(Axis.YN.rotationDegrees(90f));
        poseStack.mulPose(Axis.ZN.rotationDegrees(entity.getRotation()));
        ItemStack stack = getItemStackForRender();
        BakedModel model = itemRenderer.getModel(stack, entity.level(), null, 0);
        itemRenderer.render(stack, ItemDisplayContext.GROUND, true, poseStack, buffer, light, OverlayTexture.NO_OVERLAY, model);
        poseStack.popPose();
    }

    private ItemStack getItemStackForRender() {
        return new ItemStack(iconItem);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull AbstractBoomerangEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}