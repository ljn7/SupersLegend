package com.superworldsun.superslegend.blocks.entity;

import com.superworldsun.superslegend.registries.BlockEntityInit;
import com.superworldsun.superslegend.registries.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SwitchableFanBlockEntity extends FanBlockEntity {
    private boolean isPowered;

    public SwitchableFanBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.SWITCHABLE_FAN.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("powered", isPowered);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        isPowered = tag.getBoolean("powered");
    }

    @Override
    public boolean isPowered() {
        return isPowered;
    }

    public void setPowered(boolean powered) {
        isPowered = powered;
        setChanged();
    }

    public static BlockEntityType<SwitchableFanBlockEntity> createSwitchableFanType() {
        return BlockEntityType.Builder.of(SwitchableFanBlockEntity::new, BlockInit.SWITCHABLE_FAN.get()).build(null);
    }
}