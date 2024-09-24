package com.superworldsun.superslegend.capability.waypoint;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Waypoint {

    private final String name;
    private final BlockPos statuePos;
    private final String dimension;

    public Waypoint(String name, BlockPos statuePos, String dimension)
    {
        this.name = name;
        this.statuePos = statuePos;
        this.dimension = dimension;
    }

    public String getName()
    {
        return name;
    }

    public BlockPos getStatuePosition()
    {
        return statuePos;
    }

    public CompoundTag writeToNBT()
    {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("name", name);
        nbt.put("pos", NbtUtils.writeBlockPos(statuePos));
        nbt.putString("dimension", dimension);
        return nbt;
    }

    public static Waypoint readFromNBT(CompoundTag nbt)
    {
        String name = nbt.getString("name");
        BlockPos statuePos = NbtUtils.readBlockPos(nbt.getCompound("pos"));
        String dimension = nbt.getString("dimension");
        return new Waypoint(name, statuePos, dimension);
    }

    public String getDimension() {
        return this.dimension;
    }
}
