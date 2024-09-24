package com.superworldsun.superslegend.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class TimeBlock extends Block
{
    public static final BooleanProperty HIDDEN = BooleanProperty.create("hidden");
    protected static final VoxelShape SHAPE_FULL = Block.box(0D, 0D, 0D, 16D, 16D, 16D);
    protected static final VoxelShape SHAPE_HIDDEN = Block.box(0D, 0D, 0D, 0D, 0D, 0D);

    public TimeBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(HIDDEN, false));
    }

    public void toggle(Level level, BlockState blockState, BlockPos blockPos)
    {
        BlockState newBlockState = blockState.setValue(HIDDEN, !blockState.getValue(HIDDEN));
        level.setBlock(blockPos, newBlockState, 2);
        level.setBlocksDirty(blockPos, blockState, newBlockState);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(HIDDEN) ? Shapes.empty() : Shapes.block();
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(HIDDEN) ? SHAPE_HIDDEN : SHAPE_FULL;
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(HIDDEN);
    }
}
