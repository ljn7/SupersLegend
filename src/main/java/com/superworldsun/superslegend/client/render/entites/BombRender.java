package com.superworldsun.superslegend.client.render.entites;

import com.mojang.blaze3d.vertex.PoseStack;
import com.superworldsun.superslegend.entities.projectiles.bombs.AbstractBombEntity;
import com.superworldsun.superslegend.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

import java.time.Duration;
import java.time.Instant;

public class BombRender<T extends AbstractBombEntity> extends ThrownItemRenderer<T> {
    //Bomb rendering, entity and logic code credited to Spelunkcraft contributor ntfwc
    public static final int INITIAL_FLASHING_RATE_IN_MILLISECONDS = 20 * 1000 / 60;
    public static final int RAPID_FLASHING_RATE_IN_MILLISECONDS = 4 * 1000 / 60;

    private ItemRenderer itemRenderer;

    public BombRender(EntityRendererProvider.Context pContext, float pScale, boolean pFullBright) {
        super(pContext, pScale, pFullBright);
    }

    /*public BombRender(EntityRendererProvider.Context context, ItemRenderer itemRenderer) {
        super(context, itemRenderer);
        this.itemRenderer = itemRenderer;
    }

    public BombRender(EntityRendererProvider.Context context) {
        this(context, Minecraft.getInstance().getItemRenderer());
    }


    public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn)
    {
        float modulationValue = getFlashingModulationValue(entityIn.getCreationTime(), entityIn.shouldFlashRapidly() ? RAPID_FLASHING_RATE_IN_MILLISECONDS : INITIAL_FLASHING_RATE_IN_MILLISECONDS);
        RenderUtil.renderProjectileEntityWithModulation(entityIn, matrixStackIn, bufferIn, packedLightIn, entityRenderDispatcher, itemRenderer, 1.0F, modulationValue, modulationValue);
    }

    private float getFlashingModulationValue(Instant baseTimestamp, int frequencyInMilliSeconds) {
        Instant now = Instant.now();
        long millisSince = Duration.between(baseTimestamp, now).toMillis();
        // This creates a nice sine wave
        double modValue = (Math.sin(((millisSince * 2.0) / frequencyInMilliSeconds - 0.5) * Math.PI) + 1) / 2.0;
        int colorModValue = (int) (255 * modValue);
        return colorModValue;
    }*/
}

