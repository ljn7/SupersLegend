package com.superworldsun.superslegend.client.render.entites;

import com.mojang.blaze3d.vertex.PoseStack;
import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.client.model.items.WaterBombModel;
import com.superworldsun.superslegend.entities.projectiles.bombs.AbstractBombEntity;
import com.superworldsun.superslegend.entities.projectiles.bombs.AbstractWaterBombEntity;
import com.superworldsun.superslegend.entities.projectiles.bombs.WaterBombEntity;
import com.superworldsun.superslegend.util.RenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ItemSupplier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.time.Duration;
import java.time.Instant;

public class WaterBombRenderer<T extends AbstractWaterBombEntity & ItemSupplier> extends EntityRenderer<T>{

    public static final int INITIAL_FLASHING_RATE_IN_MILLISECONDS = 20 * 1000 / 60;
    public static final int RAPID_FLASHING_RATE_IN_MILLISECONDS = 4 * 1000 / 60;
    private ItemRenderer itemRenderer;

    public WaterBombRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public ResourceLocation getTextureLocation(T animatable) {
        return new ResourceLocation(SupersLegendMain.MOD_ID,"textures/entity/water_bomb.png");
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float modulationValue = getFlashingModulationValue(entity.getCreationTime(), entity.shouldFlashRapidly() ? RAPID_FLASHING_RATE_IN_MILLISECONDS : INITIAL_FLASHING_RATE_IN_MILLISECONDS);
        RenderUtil.renderProjectileEntityWithModulation(entity, poseStack, bufferSource, packedLight, entityRenderDispatcher, itemRenderer, 1.0F, modulationValue, modulationValue);
    }

    private float getFlashingModulationValue(Instant baseTimestamp, int frequencyInMilliSeconds) {
        Instant now = Instant.now();
        long millisSince = Duration.between(baseTimestamp, now).toMillis();
        // This creates a nice sine wave
        double modValue = (Math.sin(((millisSince * 2.0) / frequencyInMilliSeconds - 0.5) * Math.PI) + 1) / 2.0;
        int colorModValue = (int) (255 * modValue);
        return colorModValue;
    }
}