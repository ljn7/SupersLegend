package com.superworldsun.superslegend.capability.waypoint;

import com.superworldsun.superslegend.capability.waypoint.Waypoint;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoRegisterCapability
public class Waypoints implements INBTSerializable<CompoundTag> {

    private final Map<BlockPos, Waypoint> waypoints = new HashMap<>();
    private static final int MAX_WAYPOINTS = 10;

    public @NotNull List<Waypoint> getWaypoints() {
        return new ArrayList<>(waypoints.values());
    }

    public void addWaypoint(Waypoint waypoint) {
        waypoints.put(waypoint.getStatuePosition(), waypoint);

        if (waypoints.size() > getMaxWaypoints())
        {
            BlockPos firstMapKey = new ArrayList<>(waypoints.keySet()).get(0);
            waypoints.remove(firstMapKey);
        }
    }

    public void removeWaypoint(Waypoint waypoint) {
        waypoints.remove(waypoint.getStatuePosition());
    }

    public void removeWaypoint(BlockPos pos) {
        waypoints.remove(pos);
    }

    public int getMaxWaypoints() {
        return MAX_WAYPOINTS;
    }

    public Waypoint getWaypoint(BlockPos blockPos) {
        return waypoints.get(blockPos);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag waypointsList = new ListTag();

        for (Map.Entry<BlockPos, Waypoint> entry : waypoints.entrySet()) {
            CompoundTag waypointTag = new CompoundTag();

            // Serialize BlockPos
            BlockPos pos = entry.getKey();
            waypointTag.putInt("x", pos.getX());
            waypointTag.putInt("y", pos.getY());
            waypointTag.putInt("z", pos.getZ());

            // Serialize Waypoint
            Waypoint waypoint = entry.getValue();
            waypointTag.putString("name", waypoint.getName());
            waypointTag.putString("dimension", waypoint.getDimension());

            waypointsList.add(waypointTag);

        }

        nbt.put("waypoints", waypointsList);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        waypoints.clear();
        ListTag waypointsList = nbt.getList("waypoints", Tag.TAG_COMPOUND);

        for (int i = 0; i < waypointsList.size(); i++) {
            CompoundTag waypointTag = waypointsList.getCompound(i);

            // Deserialize BlockPos
            int x = waypointTag.getInt("x");
            int y = waypointTag.getInt("y");
            int z = waypointTag.getInt("z");
            BlockPos pos = new BlockPos(x, y, z);

            // Deserialize Waypoint
            String name = waypointTag.getString("name");
            String dimLocation = waypointTag.getString("dimension");
            Waypoint waypoint = new Waypoint(name, pos, dimLocation);

            waypoints.put(pos, waypoint);
        }
    }


    public void createWaypoint(BlockPos pos, String name, String dimension) {
        waypoints.put(pos, new Waypoint(name, pos, dimension));
    }
}
