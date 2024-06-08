package com.superworldsun.superslegend.mixin;

import com.superworldsun.superslegend.blocks.util.Floodable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowingFluid.class)
public abstract class MixinFlowingFluid {

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    public void tick(Level level, BlockPos blockPos, FluidState fluidState, CallbackInfo callbackInfo) {
        Block originalBlock = level.getBlockState(blockPos).getBlock();

        if (originalBlock instanceof Floodable) {
            if (!fluidState.isSource()) {
                FluidState newFluidState = getNewLiquid(level, blockPos, level.getBlockState(blockPos));
                int spreadDelay = getSpreadDelay(level, blockPos, fluidState, newFluidState);

                if (newFluidState.isEmpty()) {
                    fluidState = newFluidState;
                    level.setBlock(blockPos, originalBlock.defaultBlockState(), 3);
                } else if (!newFluidState.equals(fluidState)) {
                    fluidState = newFluidState;
                    BlockState blockstate = ((Floodable) originalBlock).getBlockState(fluidState);
                    level.setBlock(blockPos, blockstate, 2);
                    level.scheduleTick(blockPos, newFluidState.getType(), spreadDelay);
                    level.updateNeighborsAt(blockPos, blockstate.getBlock());
                }
            }

            spread(level, blockPos, fluidState);
            callbackInfo.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "canPassThroughWall", cancellable = true)
    private void injectCanPassThroughWall(Direction direction, BlockGetter world, BlockPos blockPosFrom, BlockState blockStateFrom, BlockPos blockPosTo,
                                          BlockState blockStateTo, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (blockStateFrom.getBlock() instanceof Floodable || blockStateTo.getBlock() instanceof Floodable) {
            callbackInfo.setReturnValue(true);
        }
    }

    //TODO, these 2 shadows are giving errors but dont crash game
    //@Shadow
    protected abstract FluidState getNewLiquid(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState);

    @Shadow
    protected abstract int getSpreadDelay(Level level, BlockPos blockPos, FluidState fluidState, FluidState newFluidState);

    //@Shadow
    protected abstract void spread(LevelAccessor levelAccessor, BlockPos blockPos, FluidState fluidState);
}
