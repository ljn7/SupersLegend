package com.superworldsun.superslegend.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class OpenOwlStatueModel extends Model {
    private final ModelPart Owl_Statue;

    public OpenOwlStatueModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.Owl_Statue = root.getChild("Owl_Statue");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Owl_Statue = partdefinition.addOrReplaceChild("Owl_Statue", CubeListBuilder.create()
                        .texOffs(28, 22).addBox(-9.0F, -19.0F, 0.0F, 2.0F, 6.0F, 1.0F)
                        .texOffs(0, 0).addBox(-9.0F, -20.0F, 1.0F, 2.0F, 6.0F, 3.0F)
                        .texOffs(0, 0).addBox(-13.0F, -23.0F, 4.0F, 10.0F, 11.0F, 11.0F)
                        .texOffs(31, 27).addBox(-12.0F, -12.0F, 5.0F, 8.0F, 10.0F, 9.0F)
                        .texOffs(0, 22).addBox(-12.0F, -2.0F, 2.0F, 8.0F, 2.0F, 12.0F)
                        .texOffs(0, 22).addBox(-10.0F, -19.0F, 2.0F, 4.0F, 4.0F, 2.0F),
                PartPose.offset(8.0F, 24.0F, -8.0F));

        PartDefinition cube_r1 = Owl_Statue.addOrReplaceChild("cube_r1", CubeListBuilder.create()
                        .texOffs(0, 36).addBox(-0.5F, -6.0F, -4.5F, 1.0F, 12.0F, 9.0F),
                PartPose.offsetAndRotation(0.2627F, -20.9533F, 11.1703F, -0.8699F, -1.4646F, -1.2095F));

        PartDefinition cube_r2 = Owl_Statue.addOrReplaceChild("cube_r2", CubeListBuilder.create()
                        .texOffs(20, 37).addBox(-0.5F, -6.0F, -4.5F, 1.0F, 12.0F, 9.0F),
                PartPose.offsetAndRotation(-16.2627F, -20.9533F, 11.1703F, -0.8699F, 1.4646F, 1.2095F));

        PartDefinition eyebrow1 = Owl_Statue.addOrReplaceChild("eyebrow1", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-13.2775F, -21.708F, 3.5F, 0.0F, 0.0F, -0.0873F));

        PartDefinition cube_r3 = eyebrow1.addOrReplaceChild("cube_r3", CubeListBuilder.create()
                        .texOffs(0, 28).addBox(0.5201F, -1.627F, 0.0F, 5.0F, 2.0F, 0.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.9599F));

        PartDefinition cube_r4 = eyebrow1.addOrReplaceChild("cube_r4", CubeListBuilder.create()
                        .texOffs(0, 30).addBox(-1.6996F, -1.1835F, -0.025F, 3.0F, 2.0F, 0.0F)
                        .texOffs(7, 1).addBox(-3.6996F, -1.1835F, -0.025F, 2.0F, 1.0F, 0.0F),
                PartPose.offsetAndRotation(-0.0689F, -0.0578F, 0.0F, 0.0F, 0.0F, 0.48F));

        PartDefinition eyebrow2 = Owl_Statue.addOrReplaceChild("eyebrow2", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-6.2775F, -21.708F, 3.5F, -3.1416F, 0.0F, 3.0543F));

        PartDefinition cube_r5 = eyebrow2.addOrReplaceChild("cube_r5", CubeListBuilder.create()
                        .texOffs(0, 9).addBox(-10.6622F, 6.8751F, 0.7065F, 5.0F, 2.0F, 0.0F),
                PartPose.offsetAndRotation(10.6682F, 4.6887F, -0.7075F, 0.0F, 0.0F, 0.9599F));

        PartDefinition cube_r6 = eyebrow2.addOrReplaceChild("cube_r6", CubeListBuilder.create()
                        .texOffs(28, 29).addBox(-15.5442F, 1.1945F, 0.7315F, 3.0F, 2.0F, 0.0F)
                        .texOffs(7, 0).addBox(-17.5442F, 1.1945F, 0.7315F, 2.0F, 1.0F, 0.0F),
                PartPose.offsetAndRotation(10.5993F, 4.6309F, -0.7075F, 0.0F, 0.0F, 0.48F));

        PartDefinition Book = Owl_Statue.addOrReplaceChild("Book", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r7 = Book.addOrReplaceChild("cube_r7", CubeListBuilder.create()
                        .texOffs(31, 0).addBox(-0.4198F, -3.9935F, 0.3059F, 6.0F, 8.0F, 1.0F),
                PartPose.offsetAndRotation(-8.0F, -5.7237F, 3.1787F, -0.1444F, 0.2994F, -0.0453F));

        PartDefinition cube_r8 = Book.addOrReplaceChild("cube_r8", CubeListBuilder.create()
                        .texOffs(11, 36).addBox(-5.5802F, -3.9935F, 0.3059F, 6.0F, 8.0F, 1.0F),
                PartPose.offsetAndRotation(-8.0F, -5.7237F, 3.1787F, -0.1444F, -0.2994F, 0.0453F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Owl_Statue.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}