package com.superworldsun.superslegend.blocks;


//TODO Partially ported, a couple errors
/*import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.blocks.util.Floodable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = SupersLegendMain.MOD_ID)
public class GrateBlock extends Block implements Floodable {

    public GrateBlock(Block.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FLUID_STATE_PROPERTY, 0));
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return blockState.getFluidState().isRandomlyTicking();
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        state.getFluidState().randomTick(worldIn, pos, random);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return false;
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        int fluidId = blockState.getValue(FLUID_STATE_PROPERTY);
        FluidState fluidState = Fluid.FLUID_STATE_REGISTRY.byId(fluidId);
        return fluidState;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FLUID_STATE_PROPERTY);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter p_204510_1_, BlockPos p_204510_2_, BlockState p_204510_3_, Fluid p_204510_4_) {
        return true;
    }

    @Override
    public boolean placeLiquid(LevelAccessor world, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        world.setBlock(blockPos, getBlockState(fluidState), 11);
        return true;
    }

    @Override
    public void neighborChanged(BlockState blockState, Level world, BlockPos blockPos, Block block, BlockPos neighborBlockState, boolean b) {
        Fluid fluid = blockState.getFluidState().getType();
        world.scheduleTick(blockPos, fluid, fluid.getTickDelay(world));
    }

    @Override
    public void onPlace(BlockState blockState, Level world, BlockPos blockPos, BlockState oldBlockState, boolean b) {
        Fluid fluid = blockState.getFluidState().getType();
        world.scheduleTick(blockPos, fluid, fluid.getTickDelay(world));
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return Shapes.empty();
    }

    public Fluid takeLiquid(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        FluidState fluidState = getFluidState(blockState);

        if (fluidState.isSource()) {
            levelAccessor.setBlock(blockPos, defaultBlockState(), 11);
            return fluidState.getType();
        }

        return Fluids.EMPTY;
    }

    @Override
    public BlockState getBlockState(FluidState fluidState) {
        int fluidStateId = 0;

        if (fluidState.getType() != Fluids.EMPTY) {
            fluidStateId = Fluid.FLUID_STATE_REGISTRY.getId(fluidState);
        }

        return defaultBlockState().setValue(FLUID_STATE_PROPERTY, fluidStateId);
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        Block placedBlock = event.getState().getBlock();

        if (placedBlock instanceof GrateBlock) {
            BlockState oldBlockState = event.getBlockSnapshot().getReplacedBlock();

            if (oldBlockState.getBlock() instanceof LiquidBlock) {
                GrateBlock grateBlock = (GrateBlock) placedBlock;
                BlockState floodedGrateState = grateBlock.getBlockState(oldBlockState.getFluidState());
                event.getLevel().setBlock(event.getPos(), floodedGrateState, 11);
            }
        }
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        return null;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.empty();
    }
}*/