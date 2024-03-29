package com.superworldsun.superslegend.client.render.seeds;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.entities.projectiles.seeds.CocoaBeanEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CocoaBeanRenderer extends SeedRenderer<CocoaBeanEntity> {
    public CocoaBeanRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ResourceLocation(SupersLegendMain.MOD_ID, "textures/entity/seeds/cocoa_bean.png"));
    }
}