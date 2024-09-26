package com.superworldsun.superslegend.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.superworldsun.superslegend.SupersLegendMain;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class FanModel extends Model {
    private final ModelPart blade_1;
    private final ModelPart base;

    public FanModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.blade_1 = root.getChild("blade_1");
        this.base = root.getChild("base");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition blade_1 = partdefinition.addOrReplaceChild("blade_1", CubeListBuilder.create()
                        .texOffs(7, 39).addBox(-1.5F, -6.5F, 18.0F, 3.0F, 6.0F, 1.0F),
                PartPose.offset(8.0F, 8.0F, -17.0F));

        PartDefinition blade_2 = blade_1.addOrReplaceChild("blade_2", CubeListBuilder.create()
                        .texOffs(7, 39).addBox(-1.5F, -6.5F, -0.002F, 3.0F, 6.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 18.0F, 0.0F, 0.0F, -2.0944F));

        PartDefinition blade_3 = blade_1.addOrReplaceChild("blade_3", CubeListBuilder.create()
                        .texOffs(7, 39).addBox(-1.5F, -6.5F, -0.001F, 3.0F, 6.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 18.0F, 0.0F, 0.0F, 2.0944F));

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(0.0F, -24.0F, 3.0F, 16.0F, 16.0F, 13.0F)
                        .texOffs(0, 29).addBox(15.0F, -24.0F, 0.0F, 1.0F, 16.0F, 3.0F)
                        .texOffs(0, 29).addBox(0.0F, -24.0F, 0.0F, 1.0F, 16.0F, 3.0F)
                        .texOffs(8, 29).addBox(1.0F, -9.0F, 0.0F, 14.0F, 1.0F, 3.0F)
                        .texOffs(8, 29).addBox(1.0F, -24.0F, 0.0F, 14.0F, 1.0F, 3.0F)
                        .texOffs(7, 33).addBox(7.0F, -17.0F, 0.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 58, 48);
    }

    public void render(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float partialTicks, float bladesRotation) {
        vertexConsumer.color(1.0F, 1.0F, 1.0F, 1.0F);
        blade_1.zRot = bladesRotation;
        blade_1.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        base.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {

    }
}
