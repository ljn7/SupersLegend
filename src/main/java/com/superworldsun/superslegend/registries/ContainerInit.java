package com.superworldsun.superslegend.registries;

import com.superworldsun.superslegend.SupersLegendMain;
//import com.superworldsun.superslegend.container.PostboxContainer;
import com.superworldsun.superslegend.container.bag.*;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerInit
{
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, SupersLegendMain.MOD_ID);
	public static final RegistryObject<MenuType<BagContainer>> BAG = CONTAINERS.register("bag", () -> IForgeMenuType.create(BagContainer::new));
	public static final RegistryObject<MenuType<RingBoxContainer>> RING_BOX = CONTAINERS.register("ring_box",
			() -> IForgeMenuType.create(RingBoxContainer::new));
	public static final RegistryObject<MenuType<BigRingBoxContainer>> RING_BOX_BIG = CONTAINERS.register("ring_box_big",
			() -> IForgeMenuType.create(BigRingBoxContainer::new));
	public static final RegistryObject<MenuType<BiggestRingBoxContainer>> RING_BOX_BIGGEST = CONTAINERS.register("ring_box_biggest",
			() -> IForgeMenuType.create(BiggestRingBoxContainer::new));
	public static final RegistryObject<MenuType<LetterContainer>> LETTER = CONTAINERS.register("letter",
			() -> IForgeMenuType.create(LetterContainer::new));


//	public static final RegistryObject<MenuType<PostboxContainer>> POSTBOX = CONTAINERS.register("postbox", () -> IForgeMenuType.create(PostboxContainer::new));
}
