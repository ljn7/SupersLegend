package com.superworldsun.superslegend.events;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.capability.hookshot.HookModel;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID)
public class KeyPressEvents
{

    @SubscribeEvent
    public void keyPress(InputEvent.Key event)
    {
        if(event.getKey() == GLFW.GLFW_KEY_UP && event.getAction() == GLFW.GLFW_PRESS){
            HookModel.get().setkeyUpIsDown(true);
        }
    }
}