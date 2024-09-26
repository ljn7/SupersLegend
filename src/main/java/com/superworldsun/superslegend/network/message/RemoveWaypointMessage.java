package com.superworldsun.superslegend.network.message;

import com.superworldsun.superslegend.capability.waypoint.WaypointsProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RemoveWaypointMessage {
    private final BlockPos waypointPos;

    public RemoveWaypointMessage(BlockPos waypointPos) {
        this.waypointPos = waypointPos;
    }

    public static void encode(RemoveWaypointMessage message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.waypointPos);
    }

    public static RemoveWaypointMessage decode(FriendlyByteBuf buf) {
        return new RemoveWaypointMessage(buf.readBlockPos());
    }

    public static void handle(RemoveWaypointMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.getCapability(WaypointsProvider.WAYPOINTS_CAPABILITY).ifPresent(waypoints -> {
                    waypoints.removeWaypoint(message.waypointPos);
                    WaypointsProvider.sync(player);
                });
            }
        });
        context.setPacketHandled(true);
    }
}