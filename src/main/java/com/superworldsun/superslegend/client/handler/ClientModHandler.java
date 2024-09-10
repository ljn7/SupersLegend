package com.superworldsun.superslegend.client.handler;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.client.render.blocks.FalseShadowBlockEntityRenderer;
import com.superworldsun.superslegend.client.render.blocks.HiddenShadowBlockEntityRenderer;
import com.superworldsun.superslegend.client.render.blocks.ShadowBlockEntityRenderer;
import com.superworldsun.superslegend.client.screen.PostboxScreen;
import com.superworldsun.superslegend.registries.BlockEntityInit;
import com.superworldsun.superslegend.registries.MenuTypeInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModHandler {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(MenuTypeInit.POSTBOX_MENU.get(), PostboxScreen::new);

        });
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityInit.SHADOW_ENTITY.get(), ShadowBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.HIDDEN_SHADOW.get(), HiddenShadowBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.FALSE_SHADOW.get(), FalseShadowBlockEntityRenderer::new);
    }
}