package com.superworldsun.superslegend.blocks;

import com.superworldsun.superslegend.blocks.entity.FanBlockEntity;
import com.superworldsun.superslegend.blocks.entity.SwitchableFanBlockEntity;
import com.superworldsun.superslegend.registries.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

import static com.superworldsun.superslegend.blocks.util.Ticker.createTickerHelper;

public class SwitchableFanBlock extends FanBlock implements EntityBlock {
    public SwitchableFanBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof SwitchableFanBlockEntity switchableFan) {
                boolean isPowered = level.hasNeighborSignal(pos);
                if (switchableFan.isPowered() != isPowered) {
                    switchableFan.setPowered(isPowered);
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
                }
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SwitchableFanBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BlockEntityInit.SWITCHABLE_FAN.get(), FanBlockEntity::tick);
    }

}