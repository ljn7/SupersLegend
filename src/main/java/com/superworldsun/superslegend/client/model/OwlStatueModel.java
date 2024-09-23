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

public class OwlStatueModel extends Model {
    private final ModelPart Owl_Statue;

    public OwlStatueModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.Owl_Statue = root.getChild("Owl_Statue");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Owl_Statue = partdefinition.addOrReplaceChild("Owl_Statue", CubeListBuilder.create()
                        .texOffs(28, 22).addBox(-9.0F, -19.0F, 0.0F, 2.0F, 6.0F, 1.0F)
                        .texOffs(0, 0).addBox(-9.0F, -20.0F, 1.0F, 2.0F, 6.0F, 3.0F)
                        .texOffs(0, 36).addBox(-14.0F, -17.0F, 4.0F, 1.0F, 12.0F, 9.0F)
                        .texOffs(20, 37).addBox(-3.0F, -17.0F, 4.0F, 1.0F, 12.0F, 9.0F)
                        .texOffs(0, 0).addBox(-13.0F, -23.0F, 4.0F, 10.0F, 11.0F, 11.0F)
                        .texOffs(31, 27).addBox(-12.0F, -12.0F, 5.0F, 8.0F, 10.0F, 9.0F)
                        .texOffs(0, 22).addBox(-12.0F, -2.0F, 2.0F, 8.0F, 2.0F, 12.0F)
                        .texOffs(0, 22).addBox(-10.0F, -19.0F, 2.0F, 4.0F, 4.0F, 2.0F),
                PartPose.offset(8.0F, 24.0F, -8.0F));

        // Add other parts (eyebrow1, eyebrow2, Book) similarly...

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Owl_Statue.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}