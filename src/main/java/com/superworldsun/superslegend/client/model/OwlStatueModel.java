package com.superworldsun.superslegend.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;

public class OwlStatueModel extends Model {
    private final ModelPart Owl_Statue;

    public OwlStatueModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.Owl_Statue = root.getChild("Owl_Statue");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Owl_Statue = partdefinition.addOrReplaceChild("Owl_Statue", CubeListBuilder.create().texOffs(28, 22).addBox(-9.0F, -19.0F, 0.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-9.0F, -20.0F, 1.0F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 36).addBox(-14.0F, -17.0F, 4.0F, 1.0F, 12.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(20, 37).addBox(-3.0F, -17.0F, 4.0F, 1.0F, 12.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-13.0F, -23.0F, 4.0F, 10.0F, 11.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(31, 27).addBox(-12.0F, -12.0F, 5.0F, 8.0F, 10.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-12.0F, -2.0F, 2.0F, 8.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-10.0F, -19.0F, 2.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 24.0F, -8.0F));

        PartDefinition eyebrow1 = Owl_Statue.addOrReplaceChild("eyebrow1", CubeListBuilder.create(), PartPose.offsetAndRotation(2.25F, -0.75F, 0.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition cube_r1 = eyebrow1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 28).addBox(0.562F, -1.6234F, 0.0417F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.6423F, -22.4156F, 3.4833F, 0.0F, 0.0F, 0.9599F));

        PartDefinition cube_r2 = eyebrow1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 30).addBox(-1.7519F, -1.1805F, 0.0167F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(7, 1).addBox(-3.7519F, -1.1805F, 0.0167F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.6423F, -22.4156F, 3.4833F, 0.0F, 0.0F, 0.48F));

        PartDefinition eyebrow2 = Owl_Statue.addOrReplaceChild("eyebrow2", CubeListBuilder.create(), PartPose.offsetAndRotation(4.4047F, -21.7253F, 3.4833F, 0.0F, 0.0F, -0.0873F));

        PartDefinition cube_r3 = eyebrow2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(7, 0).addBox(-10.7038F, -4.1129F, -0.4792F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(28, 29).addBox(-8.7038F, -4.1129F, -0.4792F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.7884F, 3.9297F, -0.4615F, -3.1416F, 0.0F, 2.8362F));

        PartDefinition cube_r4 = eyebrow2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 9).addBox(-6.9585F, -1.0144F, -0.5042F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.7884F, 3.9297F, -0.4615F, -3.1416F, 0.0F, 2.3562F));

        PartDefinition Book = Owl_Statue.addOrReplaceChild("Book", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r5 = Book.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(31, 0).addBox(-0.4198F, -3.9935F, 0.3059F, 6.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -5.7237F, 3.1787F, -0.1444F, 0.2994F, -0.0453F));

        PartDefinition cube_r6 = Book.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(11, 36).addBox(-5.5802F, -3.9935F, 0.3059F, 6.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -5.7237F, 3.1787F, -0.1444F, -0.2994F, 0.0453F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Owl_Statue.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
