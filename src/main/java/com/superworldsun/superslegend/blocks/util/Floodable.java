package com.superworldsun.superslegend.blocks.util;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public interface Floodable extends LiquidBlockContainer, BucketPickup {
	IntegerProperty FLUID_STATE_PROPERTY = IntegerProperty.create("fluid", 0, 1024);

	BlockState getBlockState(FluidState fluidState);
}

