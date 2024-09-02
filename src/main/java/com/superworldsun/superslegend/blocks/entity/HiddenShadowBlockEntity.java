package com.superworldsun.superslegend.blocks.entity;

import com.superworldsun.superslegend.registries.BlockEntityInit;
import com.superworldsun.superslegend.registries.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class HiddenShadowBlockEntity extends ShadowBlockEntity {
    public HiddenShadowBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.HIDDEN_SHADOW.get(), pos, state);
    }

    public static BlockEntityType<HiddenShadowBlockEntity> createHiddenShadowType() {
        return BlockEntityType.Builder.of(HiddenShadowBlockEntity::new, BlockInit.HIDDEN_SHADOW_BLOCK.get()).build(null);
    }
}