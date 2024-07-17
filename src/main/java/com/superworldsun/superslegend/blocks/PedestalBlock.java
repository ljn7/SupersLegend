package com.superworldsun.superslegend.blocks;

import com.superworldsun.superslegend.blocks.entity.PedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PedestalBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected static final VoxelShape SHAPE_NORTH = Block.box(2, 0, 5, 14, 4, 11);
    protected static final VoxelShape SHAPE_EAST = Block.box(5, 0, 2, 11, 4, 14);
    protected static final VoxelShape SHAPE_SOUTH = Block.box(2, 0, 5, 14, 4, 11);
    protected static final VoxelShape SHAPE_WEST = Block.box(5, 0, 2, 11, 4, 14);

    public PedestalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return switch (state.getValue(FACING)) {
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        ItemStack stackInHand = player.getItemInHand(hand);
        BlockEntity blockEntity = level.getBlockEntity(pos);

        System.out.println("Player interacted with PedestalBlock at " + pos);

        if (blockEntity instanceof PedestalBlockEntity) {
            PedestalBlockEntity pedestal = (PedestalBlockEntity) blockEntity;

            if (stackInHand.getItem() instanceof SwordItem && pedestal.getSword().isEmpty()) {
                System.out.println("Placing sword on pedestal");
                pedestal.setSword(stackInHand);
                player.setItemInHand(hand, ItemStack.EMPTY);
                return InteractionResult.SUCCESS;
            } else if (stackInHand.isEmpty() && !pedestal.getSword().isEmpty()) {
                System.out.println("Removing sword from pedestal");
                player.setItemInHand(hand, pedestal.getSword());
                pedestal.setSword(ItemStack.EMPTY);
                return InteractionResult.SUCCESS;
            }
        } else {
            System.out.println("BlockEntity is not an instance of PedestalBlockEntity or is null");
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof PedestalBlockEntity) {
                PedestalBlockEntity pedestal = (PedestalBlockEntity) blockEntity;
                pedestal.dropSword();
            }

            super.onRemove(state, world, pos, newState, moving);
        }
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        System.out.println("Creating new PedestalBlockEntity at " + pos);
        return new PedestalBlockEntity(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
