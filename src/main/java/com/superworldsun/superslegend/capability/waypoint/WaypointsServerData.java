package com.superworldsun.superslegend.capability.waypoint;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

public class WaypointsServerData extends SavedData {
    private final Map<BlockPos, Waypoint> waypoints = new HashMap<>();
    private static final String DATA_ID = "waypoints";

    private WaypointsServerData() {
    }

    public static WaypointsServerData create() {
        return new WaypointsServerData();
    }

    public void load(CompoundTag nbt) {
        ListTag waypointsListNbt = nbt.getList(DATA_ID, Tag.TAG_COMPOUND);
        waypoints.clear();
        waypointsListNbt.stream()
                .map(CompoundTag.class::cast)
                .map(Waypoint::readFromNBT)
                .forEach(this::addWaypoint);
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag waypointsListNbt = new ListTag();
        waypoints.values().stream()
                .map(Waypoint::writeToNBT)
                .forEach(waypointsListNbt::add);
        nbt.put(DATA_ID, waypointsListNbt);
        return nbt;
    }

    public void createWaypoint(BlockPos pos, String name, String dimension) {
        waypoints.put(pos, new Waypoint(name, pos, dimension));
        setDirty();
    }

    public Waypoint getWaypoint(BlockPos pos) {
        return waypoints.get(pos);
    }

    private void addWaypoint(Waypoint waypoint) {
        waypoints.put(waypoint.getStatuePosition(), waypoint);
    }

    public void removeWaypoint(BlockPos waypointPos) {
        waypoints.remove(waypointPos);
        setDirty();
    }

    public static WaypointsServerData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                compoundTag -> {
                    WaypointsServerData data = new WaypointsServerData();
                    data.load(compoundTag);
                    return data;
                },
                WaypointsServerData::create,
                DATA_ID
        );
    }
}