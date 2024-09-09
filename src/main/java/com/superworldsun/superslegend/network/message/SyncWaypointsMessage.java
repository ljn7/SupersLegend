package com.superworldsun.superslegend.network.message;

import com.superworldsun.superslegend.waypoints.WaypointsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SyncWaypointsMessage {
    private final List<WaypointsManager.Waypoint> waypoints;

    public SyncWaypointsMessage(Player player) {
        this.waypoints = WaypointsManager.getWaypoints(player);
    }

    public static SyncWaypointsMessage decode(FriendlyByteBuf buf) {
        int size = buf.readInt();
        List<WaypointsManager.Waypoint> waypoints = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String name = buf.readUtf();
            BlockPos pos = buf.readBlockPos();
            waypoints.add(new WaypointsManager.Waypoint(name, pos));
        }
        return new SyncWaypointsMessage(waypoints);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(waypoints.size());
        for (WaypointsManager.Waypoint waypoint : waypoints) {
            buf.writeUtf(waypoint.getName());
            buf.writeBlockPos(waypoint.getStatuePosition());
        }
    }

    public static void handle(SyncWaypointsMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(message))
        );
        ctx.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handlePacket(SyncWaypointsMessage message) {
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;
        if (player != null) {
            WaypointsManager.getWaypoints(player).clear();
            for (WaypointsManager.Waypoint waypoint : message.waypoints) {
                WaypointsManager.addWaypoint(player, waypoint);
            }
            // TODO Waypoints Screen

//            if (client.screen instanceof WaypointsScreen) {
//                client.setScreen(new WaypointsScreen());
//            }
        }
    }

    private SyncWaypointsMessage(List<WaypointsManager.Waypoint> waypoints) {
        this.waypoints = waypoints;
    }
}