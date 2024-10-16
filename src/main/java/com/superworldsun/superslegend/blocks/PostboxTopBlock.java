package com.superworldsun.superslegend.blocks;

import com.superworldsun.superslegend.blocks.entity.PostboxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PostboxTopBlock extends Block implements EntityBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public PostboxTopBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return PostboxBlock.SHAPE.move(0, -1, 0);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(new Property[]{FACING});
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			world.setBlock(pos.below(), Blocks.AIR.defaultBlockState(), 3);
		}
		super.onRemove(state, world, pos, newState, isMoving);
	}

//	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, ServerPlayer player) {
		return world.getBlockState(pos.below()).getBlock().getCloneItemStack(world.getBlockState(pos.below()), target, world, pos.below(), player);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}

		if (player instanceof ServerPlayer sPlayer) {
			getBlockEntity(level, pos).ifPresent(postbox -> postbox.interact(sPlayer, hand, state, level, pos));
		}

		return InteractionResult.CONSUME;
	}

	private Optional<PostboxBlockEntity> getBlockEntity(Level world, BlockPos pos) {
		return Optional.ofNullable(world.getBlockEntity(pos.below())).map(te -> (PostboxBlockEntity) te);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new PostboxBlockEntity(pPos, pState);
	}
}
