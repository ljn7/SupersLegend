package com.superworldsun.superslegend.blocks;


import com.superworldsun.superslegend.blocks.entity.OwlStatueBlockEntity;
import com.superworldsun.superslegend.capability.waypoint.Waypoint;
import com.superworldsun.superslegend.capability.waypoint.Waypoints;
import com.superworldsun.superslegend.capability.waypoint.WaypointsProvider;
import com.superworldsun.superslegend.client.screen.WaypointCreationScreen;
import com.superworldsun.superslegend.network.NetworkDispatcher;
import com.superworldsun.superslegend.network.message.ShowWaystoneCreationScreenMessage;

import net.minecraft.client.Minecraft;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;


public class OwlStatueBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16D, 20.0D, 16D);

    public OwlStatueBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity entity, ItemStack stack) {
        if (entity instanceof Player && level.isClientSide()) {
            BlockPos waypointPos = blockPos.relative(blockState.getValue(FACING));
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> showWaypointCreationScreen(waypointPos, (Player) entity));
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockPos waypointPos = blockPos.relative(blockState.getValue(FACING));
            Waypoint waypoint =  WaypointsProvider.get(player).getWaypoint(waypointPos);
            Waypoints savedWaypoints = WaypointsProvider.get(player);
            if (waypoint != null)
            {
                if (savedWaypoints.getWaypoint(waypointPos) == null)
                {
                    if (!level.isClientSide)
                    {
                        // if already maximum waypoints
                        if (savedWaypoints.getWaypoints().size() == savedWaypoints.getMaxWaypoints())
                        {
                            player.sendSystemMessage(Component.translatable("block.superslegend.owl_statue.maximum", savedWaypoints.getMaxWaypoints()));
                        }
                        else
                        {
                            savedWaypoints.addWaypoint(waypoint);
                            WaypointsProvider.sync((ServerPlayer) player);
                            player.sendSystemMessage(Component.translatable("block.superslegend.owl_statue.saved", waypoint.getName()));
                        }
                    }
                }
            }
            else
            {
                NetworkDispatcher.network_channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new ShowWaystoneCreationScreenMessage(waypointPos, player.getUUID()));
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide()) {
                BlockPos waypointPos = pos.relative(state.getValue(FACING));

                // Remove the waypoint from all online players
                for (ServerPlayer player : ((ServerLevel)level).getServer().getPlayerList().getPlayers()) {
                    player.getCapability(WaypointsProvider.WAYPOINTS_CAPABILITY).ifPresent(waypoints -> {
                        Waypoint waypoint = waypoints.getWaypoint(waypointPos);
                        if (waypoint != null) {
                            waypoints.removeWaypoint(waypointPos);
                            WaypointsProvider.sync(player);
                            player.sendSystemMessage(Component.translatable("block.superslegend.owl_statue.removed", waypoint.getName()));
                        }
                    });
                }
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OwlStatueBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return !level.isEmptyBlock(pos.below());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.box(0, 0, 0, 1, 1.25, 1);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @OnlyIn(Dist.CLIENT)
    private void showWaypointCreationScreen(BlockPos blockPos, Player player) {
        net.minecraft.client.Minecraft.getInstance().setScreen(new WaypointCreationScreen(blockPos, player));
    }
}