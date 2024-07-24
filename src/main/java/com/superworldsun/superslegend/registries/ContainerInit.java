package com.superworldsun.superslegend.registries;

import com.superworldsun.superslegend.SupersLegendMain;
//import com.superworldsun.superslegend.container.PostboxContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerInit
{
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, SupersLegendMain.MOD_ID);

//	public static final RegistryObject<MenuType<PostboxContainer>> POSTBOX = CONTAINERS.register("postbox", () -> IForgeMenuType.create(PostboxContainer::new));
}
