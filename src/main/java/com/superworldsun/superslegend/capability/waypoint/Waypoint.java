package com.superworldsun.superslegend.capability.waypoint;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Waypoint {

    private final String name;
    private final BlockPos statuePos;
    private final String dimension;
    private final Vec3 teleportPos;
    private Direction facing;

    public Waypoint(String name, BlockPos statuePos, Vec3 teleportPos, Direction facing, String dimension)
    {
        this.name = name;
        this.statuePos = statuePos;
        this.dimension = dimension;
        this.teleportPos = teleportPos;
        this.facing = facing;
    }

    public String getName()
    {
        return name;
    }

    public BlockPos getStatuePosition()
    {
        return statuePos;
    }

    public Vec3 getTeleportPos() {
        return teleportPos;
    }

    public CompoundTag writeToNBT()
    {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("name", name);
        nbt.put("pos", NbtUtils.writeBlockPos(statuePos));
        nbt.putString("dimension", dimension);

        CompoundTag teleportTag = new CompoundTag();
        teleportTag.putDouble("x", teleportPos.x);
        teleportTag.putDouble("y", teleportPos.y);
        teleportTag.putDouble("z", teleportPos.z);
        nbt.put("teleportPos", teleportTag);

        nbt.putInt("facing", facing.get3DDataValue());

        return nbt;
    }

    public static Waypoint readFromNBT(CompoundTag nbt)
    {
        String name = nbt.getString("name");
        BlockPos statuePos = NbtUtils.readBlockPos(nbt.getCompound("pos"));
        String dimension = nbt.getString("dimension");

        CompoundTag teleportTag = nbt.getCompound("teleportPos");
        Vec3 teleportPos = new Vec3(
                teleportTag.getDouble("x"),
                teleportTag.getDouble("y"),
                teleportTag.getDouble("z")
        );

        Direction facing = Direction.from3DDataValue(nbt.getInt("facing"));

        return new Waypoint(name, statuePos, teleportPos, facing, dimension);
    }

    public String getDimension() {
        return this.dimension;
    }

    public Direction getFacing() {
        return this.facing;
    }
}
