package com.superworldsun.superslegend.capability.hookshot;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.network.message.SyncHookshot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID)
public class Hook
{
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof Player)
        {
            HookModel skillModel = new HookModel();
            HookProvider provider = new HookProvider(skillModel);
            event.addCapability(new ResourceLocation("zelda_hs", "cap_hook"), provider);
            event.addListener(provider::invalidate);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e)
    {
        SyncHookshot.send(e.getEntity());
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent e)
    {
        SyncHookshot.send(e.getEntity());
    }
}