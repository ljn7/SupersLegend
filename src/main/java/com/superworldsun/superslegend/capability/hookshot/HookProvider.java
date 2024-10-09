package com.superworldsun.superslegend.capability.hookshot;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HookProvider implements ICapabilitySerializable<CompoundTag> {
    private final HookModel hookModel;
    private final LazyOptional<HookModel> optional;

    public HookProvider(HookModel hookModel) {
        this.hookModel = hookModel;
        optional = LazyOptional.of(() -> hookModel);
    }

    public void invalidate() {
        optional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        if (capability == HookCapability.INSTANCE) return optional.cast();
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return hookModel.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        hookModel.deserializeNBT(nbt);
    }
}
