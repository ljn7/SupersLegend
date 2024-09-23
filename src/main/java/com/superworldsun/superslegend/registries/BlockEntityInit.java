package com.superworldsun.superslegend.registries;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.blocks.entity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit
{
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SupersLegendMain.MOD_ID);

	public static final RegistryObject<BlockEntityType<PedestalBlockEntity>> PEDESTAL_ENTITY = BLOCK_ENTITIES.register("pedestal", () ->
			BlockEntityType.Builder.of(PedestalBlockEntity::new,BlockInit.PEDESTAL.get()).build(null));
	public static final RegistryObject<BlockEntityType<PostboxBlockEntity>> POSTBOX_ENTITY = BLOCK_ENTITIES.register("postbox", () ->
			BlockEntityType.Builder.of(PostboxBlockEntity::new,BlockInit.POSTBOX_BLOCK.get()).build(null));

	public static final RegistryObject<BlockEntityType<ShadowBlockEntity>> SHADOW_ENTITY = BLOCK_ENTITIES.register("shadow", ShadowBlockEntity::createShadowType);
	public static final RegistryObject<BlockEntityType<FalseShadowBlockEntity>> FALSE_SHADOW = BLOCK_ENTITIES.register("false_shadow", FalseShadowBlockEntity::createFalseShadowType);
	public static final RegistryObject<BlockEntityType<HiddenShadowBlockEntity>> HIDDEN_SHADOW = BLOCK_ENTITIES.register("hidden_shadow", HiddenShadowBlockEntity::createHiddenShadowType);
	public static final RegistryObject<BlockEntityType<OwlStatueBlockEntity>> OWL_STATUE = BLOCK_ENTITIES.register("owl_statue", OwlStatueBlockEntity::createType);


	public static void register(IEventBus eventBus) {
		BLOCK_ENTITIES.register(eventBus);
	}
}
