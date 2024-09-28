package com.superworldsun.superslegend.network.message;

import com.superworldsun.superslegend.capability.waypoint.WaypointsProvider;
import com.superworldsun.superslegend.capability.waypoint.WaypointsServerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SetWaypointNameMessage {
    private final BlockPos pos;
    private final String text;
    private final UUID playerUUID;
    public SetWaypointNameMessage(BlockPos pos, String text, UUID playerUUID) {
        this.pos = pos;
        this.text = text;
        this.playerUUID = playerUUID;
    }

    public static void encode(SetWaypointNameMessage message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
        buf.writeUtf(message.text);
        buf.writeUUID(message.playerUUID);
    }

    public static SetWaypointNameMessage decode(FriendlyByteBuf buf) {
        return new SetWaypointNameMessage(buf.readBlockPos(), buf.readUtf(), buf.readUUID());
    }

    public static void handle(SetWaypointNameMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.setPacketHandled(true);
        context.enqueueWork(() -> {
            ServerLevel level = context.getSender().serverLevel();
            context.enqueueWork(() -> WaypointsServerData.get(level).createWaypoint(message.pos, message.text, level.dimension().toString()));
        });

    }
}
