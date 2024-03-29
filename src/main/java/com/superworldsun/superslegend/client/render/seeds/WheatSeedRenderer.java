package com.superworldsun.superslegend.client.render.seeds;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.entities.projectiles.seeds.WheatSeedEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class WheatSeedRenderer extends SeedRenderer<WheatSeedEntity> {
    public WheatSeedRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ResourceLocation(SupersLegendMain.MOD_ID, "textures/entity/seeds/wheat_seed.png"));
    }
}