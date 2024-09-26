package com.superworldsun.superslegend.client.events;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.registries.AttributeInit;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AttributesEvents
{
    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event)
    {
        event.add(EntityType.PLAYER, AttributeInit.COLD_RESISTANCE.get());
        event.add(EntityType.PLAYER, AttributeInit.HEAT_RESISTANCE.get());
        event.add(EntityType.PLAYER, AttributeInit.HELL_HEAT_RESISTANCE.get());
    }
}
