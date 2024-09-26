package com.superworldsun.superslegend.blocks.entity;

import com.superworldsun.superslegend.registries.BlockEntityInit;
import com.superworldsun.superslegend.registries.BlockInit;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class OwlStatueBlockEntity extends BlockEntity {
    public OwlStatueBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.OWL_STATUE.get(), pos, state);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition, worldPosition.offset(1, 1, 1));
    }

    public static BlockEntityType<OwlStatueBlockEntity> createType() {
        return BlockEntityType.Builder.of(OwlStatueBlockEntity::new, BlockInit.OWL_STATUE.get()).build(null);
    }
}
