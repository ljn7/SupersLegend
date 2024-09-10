package com.superworldsun.superslegend.waypoints;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.network.NetworkDispatcher;
import com.superworldsun.superslegend.network.message.SyncWaypointsMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID)
public class WaypointsManager {
    private static final String NBT_KEY = "Waypoints";
    private static final int MAX_WAYPOINTS = 10;
    private static final Map<UUID, List<Waypoint>> playerWaypoints = new HashMap<>();

    public static List<Waypoint> getWaypoints(Player player) {
        return playerWaypoints.getOrDefault(player.getUUID(), new ArrayList<>());
    }

    public static void addWaypoint(Player player, Waypoint waypoint) {
        List<Waypoint> waypoints = playerWaypoints.computeIfAbsent(player.getUUID(), k -> new ArrayList<>());
        waypoints.add(waypoint);
        if (waypoints.size() > MAX_WAYPOINTS) {
            waypoints.remove(0);
        }
        savePlayerData((ServerPlayer) player);
    }

    public static void removeWaypoint(Player player, Waypoint waypoint) {
        List<Waypoint> waypoints = playerWaypoints.get(player.getUUID());
        if (waypoints != null) {
            waypoints.remove(waypoint);
            savePlayerData((ServerPlayer) player);
        }
    }

    public static void removeWaypoint(Player player, BlockPos pos) {
        List<Waypoint> waypoints = playerWaypoints.get(player.getUUID());
        if (waypoints != null) {
            waypoints.removeIf(w -> w.getStatuePosition().equals(pos));
            savePlayerData((ServerPlayer) player);
        }
    }

    public static Waypoint getWaypoint(Player player, BlockPos blockPos) {
        List<Waypoint> waypoints = playerWaypoints.get(player.getUUID());
        if (waypoints != null) {
            return waypoints.stream()
                    .filter(w -> w.getStatuePosition().equals(blockPos))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    private static void savePlayerData(ServerPlayer player) {
        CompoundTag playerData = player.getPersistentData();
        CompoundTag modData = playerData.getCompound(Player.PERSISTED_NBT_TAG);

        ListTag waypointsList = new ListTag();
        List<Waypoint> waypoints = playerWaypoints.get(player.getUUID());
        if (waypoints != null) {
            for (Waypoint waypoint : waypoints) {
                waypointsList.add(waypoint.writeToNBT());
            }
        }

        modData.put(NBT_KEY, waypointsList);
        playerData.put(Player.PERSISTED_NBT_TAG, modData);
    }

    private static void loadPlayerData(ServerPlayer player) {
        CompoundTag playerData = player.getPersistentData();
        CompoundTag modData = playerData.getCompound(Player.PERSISTED_NBT_TAG);

        if (modData.contains(NBT_KEY, Tag.TAG_LIST)) {
            ListTag waypointsList = modData.getList(NBT_KEY, Tag.TAG_COMPOUND);
            List<Waypoint> waypoints = new ArrayList<>();

            for (int i = 0; i < waypointsList.size(); i++) {
                CompoundTag waypointTag = waypointsList.getCompound(i);
                waypoints.add(Waypoint.readFromNBT(waypointTag));
            }

            playerWaypoints.put(player.getUUID(), waypoints);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            loadPlayerData((ServerPlayer) event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        playerWaypoints.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            loadPlayerData((ServerPlayer) event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        Player original = event.getOriginal();
        Player player = event.getEntity();

        CompoundTag originalData = original.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        CompoundTag newData = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);

        if (originalData.contains(NBT_KEY)) {
            newData.put(NBT_KEY, originalData.get(NBT_KEY));
            player.getPersistentData().put(Player.PERSISTED_NBT_TAG, newData);
        }
    }

    public static class Waypoint {
        private final String name;
        private final BlockPos statuePos;

        public Waypoint(String name, BlockPos statuePos) {
            this.name = name;
            this.statuePos = statuePos;
        }

        public String getName() {
            return name;
        }

        public BlockPos getStatuePosition() {
            return statuePos;
        }

        public CompoundTag writeToNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("name", name);
            nbt.putInt("x", statuePos.getX());
            nbt.putInt("y", statuePos.getY());
            nbt.putInt("z", statuePos.getZ());
            return nbt;
        }

        public static Waypoint readFromNBT(CompoundTag nbt) {
            String name = nbt.getString("name");
            BlockPos statuePos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
            return new Waypoint(name, statuePos);
        }
    }

    public static void syncToClient(ServerPlayer player) {
        List<Waypoint> waypoints = getWaypoints(player);
        NetworkDispatcher.network_channel.send(
                PacketDistributor.PLAYER.with(() -> player),
                new SyncWaypointsMessage(player)
        );
    }
}