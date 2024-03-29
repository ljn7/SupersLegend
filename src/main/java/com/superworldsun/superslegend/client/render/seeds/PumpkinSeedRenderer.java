package com.superworldsun.superslegend.client.render.seeds;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.entities.projectiles.seeds.PumpkinSeedEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PumpkinSeedRenderer extends SeedRenderer<PumpkinSeedEntity> {
    public PumpkinSeedRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ResourceLocation(SupersLegendMain.MOD_ID, "textures/entity/seeds/pumpkin_seed.png"));
    }
}