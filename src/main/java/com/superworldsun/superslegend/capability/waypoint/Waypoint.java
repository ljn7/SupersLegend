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
    private ResourceKey<Level> dimension = Level.OVERWORLD;

    public Waypoint(String name, BlockPos statuePos, ResourceKey<Level> dimension)
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
        nbt.putString("dimension", dimension.location().toString());
        return nbt;
    }

    public static Waypoint readFromNBT(CompoundTag nbt)
    {
        String name = nbt.getString("name");
        BlockPos statuePos = NbtUtils.readBlockPos(nbt.getCompound("pos"));
        ResourceLocation dimLocation = new ResourceLocation(nbt.getString("dimension"));
        ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, dimLocation);
        return new Waypoint(name, statuePos, dimension);
    }

    public ResourceKey<Level> getDimension() {
        return this.dimension;
    }
}
