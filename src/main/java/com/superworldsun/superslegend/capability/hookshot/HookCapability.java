package com.superworldsun.superslegend.capability.hookshot;

import com.superworldsun.superslegend.SupersLegendMain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HookCapability {
    public static final Capability<HookModel> INSTANCE = CapabilityManager.get(new CapabilityToken<>(){});

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.register(HookModel.class);
    }
}