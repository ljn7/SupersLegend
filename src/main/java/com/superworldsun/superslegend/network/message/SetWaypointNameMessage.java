package com.superworldsun.superslegend.network.message;

import com.superworldsun.superslegend.waypoints.WaypointsManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetWaypointNameMessage {
    private final BlockPos pos;
    private final String text;

    public SetWaypointNameMessage(BlockPos pos, String text) {
        this.pos = pos;
        this.text = text;
    }

    public static SetWaypointNameMessage decode(FriendlyByteBuf buf) {
        return new SetWaypointNameMessage(buf.readBlockPos(), buf.readUtf());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(text);
    }

    public static void handle(SetWaypointNameMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                WaypointsManager.addWaypoint(player, new WaypointsManager.Waypoint(message.text, message.pos));
                WaypointsManager.syncToClient(player);
            }
        });
        ctx.setPacketHandled(true);
    }
}
