package com.superworldsun.superslegend.client.render.seeds;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.entities.projectiles.seeds.MelonSeedEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MelonSeedRenderer extends SeedRenderer<MelonSeedEntity> {
    public MelonSeedRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ResourceLocation(SupersLegendMain.MOD_ID, "textures/entity/seeds/melon_seed.png"));
    }
}