package com.superworldsun.superslegend.client.model.items;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.entities.projectiles.bombs.WaterBombEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WaterBombModel extends GeoModel<WaterBombEntity> {
    @Override
    public ResourceLocation getModelResource(WaterBombEntity animatable) {
        return new ResourceLocation(SupersLegendMain.MOD_ID,"geo/bomb.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WaterBombEntity animatable) {
        return new ResourceLocation(SupersLegendMain.MOD_ID,"textures/entity/bomb.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WaterBombEntity animatable) {
        return new ResourceLocation(SupersLegendMain.MOD_ID,"animations/bomb.animation.json");
    }
}
