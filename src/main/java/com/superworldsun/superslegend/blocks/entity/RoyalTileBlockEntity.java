package com.superworldsun.superslegend.blocks.entity;

import com.superworldsun.superslegend.blocks.RoyalTileBlock;
import com.superworldsun.superslegend.registries.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RoyalTileBlockEntity extends BlockEntity {
    private int ticksRemaining = -1;

    public RoyalTileBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.ROYAL_TILE_BLOCK_ENTITY.get(), pos, state);
    }

    public void scheduleTick() {
        this.ticksRemaining = 20;
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (this.ticksRemaining > 0) {
            --this.ticksRemaining;
            if (this.ticksRemaining == 0) {
                BlockState newState = state.setValue(RoyalTileBlock.POWERED, false);
                level.setBlock(pos, newState, 3);
                level.updateNeighborsAt(pos, newState.getBlock());
                level.updateNeighborsAt(pos.below(), newState.getBlock());
            }
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.ticksRemaining = tag.getInt("TicksRemaining");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("TicksRemaining", this.ticksRemaining);
    }
}