package com.superworldsun.superslegend.blocks.entity;

import com.superworldsun.superslegend.registries.BlockEntityInit;
import com.superworldsun.superslegend.registries.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FalseShadowBlockEntity  extends ShadowBlockEntity  {
    public FalseShadowBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.FALSE_SHADOW.get(), pos, state);
    }

    public static BlockEntityType<FalseShadowBlockEntity> createFalseShadowType() {
        return BlockEntityType.Builder.of(FalseShadowBlockEntity::new, BlockInit.FALSE_SHADOW_BLOCK.get()).build(null);
    }
}
