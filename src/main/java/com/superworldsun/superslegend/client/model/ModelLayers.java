package com.superworldsun.superslegend.client.model;

import com.superworldsun.superslegend.SupersLegendMain;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class ModelLayers {
    public static final ModelLayerLocation OWL_STATUE_CLOSED = new ModelLayerLocation(new ResourceLocation(SupersLegendMain.MOD_ID, "owl_statue_closed"), "main");
    public static final ModelLayerLocation OWL_STATUE_OPEN = new ModelLayerLocation(new ResourceLocation(SupersLegendMain.MOD_ID, "owl_statue_open"), "main");
}
