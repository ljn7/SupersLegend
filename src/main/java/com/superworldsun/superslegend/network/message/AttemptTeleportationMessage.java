package com.superworldsun.superslegend.network.message;

import com.superworldsun.superslegend.capability.waypoint.Waypoint;
import com.superworldsun.superslegend.capability.waypoint.WaypointsProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collections;
import java.util.UUID;
import java.util.function.Supplier;

public class AttemptTeleportationMessage {
    private final BlockPos pos;
    private final UUID playerUUID;

    public AttemptTeleportationMessage(BlockPos pos, UUID playerUUID) {
        this.pos = pos;
        this.playerUUID = playerUUID;
    }

    public static void encode(AttemptTeleportationMessage message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
        buf.writeUUID(message.playerUUID);
    }

    public static AttemptTeleportationMessage decode(FriendlyByteBuf buf) {
        return new AttemptTeleportationMessage(buf.readBlockPos(), buf.readUUID());
    }

    public static void handle(AttemptTeleportationMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
                    ServerPlayer player = context.getSender();
                    if (player != null) {
                        player.getCapability(WaypointsProvider.WAYPOINTS_CAPABILITY).ifPresent(waypoints -> {
                            Waypoint waypoint = waypoints.getWaypoint(message.pos);
                            if (waypoint != null) {
                                if (player.level().dimension().toString().equals(waypoint.getDimension())) {
                                    player.teleportTo(message.pos.getX(), message.pos.getY(), message.pos.getZ());
                                    player.connection.send(new ClientboundPlayerPositionPacket(message.pos.getX(), message.pos.getY(), message.pos.getZ(), 0, 0, Collections.emptySet(), 0));
                                    player.displayClientMessage(Component.literal("Teleported to waypoint").withStyle(ChatFormatting.DARK_GREEN)
                                            .append(" x: " + message.pos.getX())
                                            .append(" y: " + message.pos.getY())
                                            .append(" z: " + message.pos.getZ()), true);
                                    System.out.println("Teleported player to: " + waypoint);
                                    System.out.println(waypoint.getDimension());
                                } else {
                                    System.out.println(player.level().dimension());
                                }
                            } else {
                                System.out.println("Waypoint not found at position: " + message.pos);
                                waypoints.removeWaypoint(message.pos);
                            }
                        });
                    } else {
                        System.out.println("Player not found");
                    }
                });

        context.setPacketHandled(true);
    }
}