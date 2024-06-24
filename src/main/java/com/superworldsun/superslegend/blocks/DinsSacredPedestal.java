package com.superworldsun.superslegend.blocks;

import com.superworldsun.superslegend.entities.projectiles.magic.MasterSwordBeamEntity;
import com.superworldsun.superslegend.registries.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class DinsSacredPedestal extends Block {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    protected static final VoxelShape FACE_EAST_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape FACE_WEST_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape FACE_SOUTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape FACE_NORTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public DinsSacredPedestal(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        switch(state.getValue(FACING)) {
            case EAST:
            default:
                return FACE_EAST_AABB;
            case WEST:
                return FACE_WEST_AABB;
            case SOUTH:
                return FACE_SOUTH_AABB;
            case NORTH:
                return FACE_NORTH_AABB;
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if(entity instanceof MasterSwordBeamEntity && world.isEmptyBlock(pos.above()))
        {
            world.setBlockAndUpdate(pos.above(), BlockInit.DINS_FLAME.get().defaultBlockState());
            BlockPos currentPos = entity.blockPosition();
            world.playSound(null, currentPos.getX(), currentPos.getY(), currentPos.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1f, 1f);
        }
    }

}