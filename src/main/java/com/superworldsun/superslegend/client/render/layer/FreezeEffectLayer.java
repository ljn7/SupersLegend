package com.superworldsun.superslegend.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.registries.EffectInit;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;


public class FreezeEffectLayer extends RenderLayer<LivingEntity, EntityModel<LivingEntity>>  {
    private static final ResourceLocation FREEZE_TEXTURE = new ResourceLocation(SupersLegendMain.MOD_ID, "textures/entity/layer/frozen.png");

    public FreezeEffectLayer(LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.hasEffect(EffectInit.FREEZE.get())) {
            return;
        }
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(FREEZE_TEXTURE));
        this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}