package com.superworldsun.superslegend.capability.waypoint;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.network.NetworkDispatcher;
import com.superworldsun.superslegend.network.message.SyncWaypointsMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = SupersLegendMain.MOD_ID)
public class WaypointsProvider extends SavedData implements ICapabilitySerializable<CompoundTag>, ICapabilityProvider {

    private static final ResourceLocation WAYPOINTS_ID = new ResourceLocation(SupersLegendMain.MOD_ID, "waypoints");
    public static Capability<Waypoints> WAYPOINTS_CAPABILITY = CapabilityManager.get(new CapabilityToken<Waypoints>() { });

    private Waypoints waypoints = null;
    private final LazyOptional<Waypoints> optional = LazyOptional.of(this::createWaypoints);

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        // We add waypoints only to players
        if (!(event.getObject() instanceof Player))
        {
            return;
        }

        event.addCapability(WAYPOINTS_ID, new WaypointsProvider());
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event)
    {
        // We add waypoints only to players, means we only sync it for players
        if (!(event.getEntity() instanceof Player))
        {
            return;
        }

        // We only sync from server to client, not other way around!
        if (event.getEntity().level().isClientSide)
        {
            return;
        }

        Player player = (Player) event.getEntity();
        WaypointsProvider.sync((ServerPlayer) player);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == WAYPOINTS_CAPABILITY) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    private Waypoints createWaypoints() {
        if(this.waypoints == null) {
            this.waypoints = new Waypoints();
        }
        return this.waypoints;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag waypointsListNbt = new ListTag();
        createWaypoints().getWaypoints().forEach(waypoint -> waypointsListNbt.add(waypoint.writeToNBT()));
        nbt.put("waypoints", waypointsListNbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag waypointsListNbt = nbt.getList("waypoints", new CompoundTag().getId());
        createWaypoints().getWaypoints().forEach(waypoint -> createWaypoints().removeWaypoint(waypoint));
        waypointsListNbt.forEach(waypointNbt -> createWaypoints().addWaypoint(Waypoint.readFromNBT((CompoundTag) waypointNbt)));
    }

    @Override
    public CompoundTag save(@NotNull CompoundTag pCompoundTag) {
        ListTag waypointsListNbt = new ListTag();
        createWaypoints().getWaypoints().forEach(waypoint -> waypointsListNbt.add(waypoint.writeToNBT()));
        pCompoundTag.put("waypoints", waypointsListNbt);
        return pCompoundTag;
    }

    public static Waypoints get(Player player)
    {
        return player.getCapability(WAYPOINTS_CAPABILITY).orElse(new Waypoints());
    }

    public static void sync(ServerPlayer player)
    {
        NetworkDispatcher.network_channel.send(PacketDistributor.PLAYER.with(() -> player), new SyncWaypointsMessage(player));
    }

//    public void createWaypoint(BlockPos pos, String name) {
//        waypoints.createWaypoint(pos, name);
//        setDirty();
//    }
}
