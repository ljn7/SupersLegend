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

    @Shadow protected abstract void spread(Level pLevel, BlockPos pPos, FluidState pState);
    @Shadow protected abstract FluidState getNewLiquid(Level pLevel, BlockPos pPos, BlockState pBlockState);
    @Shadow protected abstract boolean canPassThroughWall(Direction pDirection, BlockGetter pLevel, BlockPos p_76064_, BlockState p_76065_, BlockPos p_76066_, BlockState p_76067_);

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void onTick(Level level, BlockPos pos, FluidState state, CallbackInfo ci) {
        Block block = level.getBlockState(pos).getBlock();
        if (block instanceof Floodable) {
            if (!state.isSource()) {
                FluidState newFluidState = this.getNewLiquid(level, pos, level.getBlockState(pos));
                if (newFluidState.isEmpty()) {
                    level.setBlock(pos, block.defaultBlockState(), 3);
                    ci.cancel();
                } else if (!newFluidState.equals(state)) {
                    BlockState blockstate = ((Floodable) block).getBlockState(newFluidState);
                    level.setBlock(pos, blockstate, 2);
                    level.scheduleTick(pos, newFluidState.getType(), this.getSpreadDelay(level, pos, state, newFluidState));
                    level.updateNeighborsAt(pos, blockstate.getBlock());
                    ci.cancel();
                }
            }
            this.spread(level, pos, state);
            ci.cancel();
        }
    }

    @Inject(method = "canPassThroughWall", at = @At("HEAD"), cancellable = true)
    private void onCanPassThroughWall(Direction direction, BlockGetter level, BlockPos fromPos, BlockState fromState, BlockPos toPos, BlockState toState, CallbackInfoReturnable<Boolean> cir) {
        if (fromState.getBlock() instanceof Floodable || toState.getBlock() instanceof Floodable) {
            cir.setReturnValue(true);
        }
    }

    @Shadow protected abstract int getSpreadDelay(Level pLevel, BlockPos pPos, FluidState p_76000_, FluidState p_76001_);
}
