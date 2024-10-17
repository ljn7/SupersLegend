package com.superworldsun.superslegend.blocks;

import com.superworldsun.superslegend.blocks.entity.RoyalTileBlockEntity;
import com.superworldsun.superslegend.util.BlockShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RoyalTileBlock extends Block implements EntityBlock {
    private static final VoxelShape SHAPE = Block.box(0D, 0D, 0D, 16D, 16D, 0.5D);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
    public static final DirectionProperty ROTATION = DirectionProperty.create("rotation", Direction.Plane.HORIZONTAL);

    public RoyalTileBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(POWERED, false)
                .setValue(FACING, Direction.SOUTH)
                .setValue(ROTATION, Direction.SOUTH));
    }

    public void activate(Level level, BlockState blockState, BlockPos blockPos) {
        BlockState newBlockState = setSignalForState(blockState, 15);
        level.setBlock(blockPos, newBlockState, 2);
        updateNeighbours(level, blockPos);
        level.setBlocksDirty(blockPos, blockState, newBlockState);
        if (level.getBlockEntity(blockPos) instanceof RoyalTileBlockEntity royalTileEntity) {
            royalTileEntity.scheduleTick();
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RoyalTileBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (level1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof RoyalTileBlockEntity royalTileEntity) {
                royalTileEntity.serverTick(level1, pos, state1);
            }
        };
    }

    protected int getSignalForState(BlockState blockState) {
        return blockState.getValue(POWERED) ? 15 : 0;
    }

    protected void updateNeighbours(Level level, BlockPos blockPos) {
        level.updateNeighborsAt(blockPos, this);
        level.updateNeighborsAt(blockPos.below(), this);
    }

    protected BlockState setSignalForState(BlockState blockState, int signal) {
        return blockState.setValue(POWERED, signal > 0);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return BlockShapeHelper.rotateShape(Direction.SOUTH, blockState.getValue(FACING), SHAPE);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newBlockState, boolean isMoving) {
        if (!isMoving && !blockState.is(newBlockState.getBlock())) {
            if (getSignalForState(blockState) > 0) {
                updateNeighbours(level, blockPos);
            }
            super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
        }
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return getSignalForState(blockState);
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return direction == Direction.UP ? getSignalForState(blockState) : 0;
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED).add(FACING).add(ROTATION);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.DESTROY;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getClickedFace())
                .setValue(ROTATION, context.getHorizontalDirection());
    }
}