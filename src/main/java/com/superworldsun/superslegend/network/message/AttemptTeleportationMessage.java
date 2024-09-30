package com.superworldsun.superslegend.network.message;

import com.superworldsun.superslegend.capability.waypoint.Waypoint;
import com.superworldsun.superslegend.capability.waypoint.WaypointsProvider;
import com.superworldsun.superslegend.capability.waypoint.WaypointsServerData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public class AttemptTeleportationMessage {
    private final BlockPos pos;

    public AttemptTeleportationMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(AttemptTeleportationMessage message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
    }

    public static AttemptTeleportationMessage decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        return new AttemptTeleportationMessage(pos);
    }

    public static void handle(AttemptTeleportationMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            if (player != null) {
                Waypoint serverWaypoint = WaypointsServerData.get(player.serverLevel()).getWaypoint(message.pos);
                if (serverWaypoint == null) {
                    WaypointsProvider.get(player).removeWaypoint(message.pos);
                    player.displayClientMessage(Component.literal("Waypoint not found on server").withStyle(ChatFormatting.RED), true);
                } else {
                    player.getCapability(WaypointsProvider.WAYPOINTS_CAPABILITY).ifPresent(waypoints -> {
                        Waypoint waypoint = waypoints.getWaypoint(message.pos);
                        if (waypoint != null) {
                            if (player.level().dimension().location().toString().equals(waypoint.getDimension())) {
                                // Use the teleportPos from the waypoint

                                float yaw = waypoint.getFacing().getOpposite().toYRot();
                                player.setYRot(yaw);
                                player.setYHeadRot(yaw);

                                // Update client
                                player.connection.send(new ClientboundPlayerPositionPacket(
                                        waypoint.getTeleportPos().x, waypoint.getTeleportPos().y, waypoint.getTeleportPos().z,
                                        yaw, player.getXRot(), Set.of(), 0
                                ));

                                player.displayClientMessage(Component.literal("Teleported to waypoint").withStyle(ChatFormatting.DARK_GREEN)
                                        .append(String.format(" x: %d", (int) waypoint.getTeleportPos().x))
                                        .append(String.format(" y: %d", (int) waypoint.getTeleportPos().y))
                                        .append(String.format(" z: %d", (int) waypoint.getTeleportPos().z)), true);
                                System.out.println("Teleported player to: " + waypoint);
                            } else {
                                System.out.println("Player dimension: " + player.level().dimension().location());
                                System.out.println("Waypoint dimension: " + waypoint.getDimension());
                                player.displayClientMessage(Component.literal("Cannot teleport between dimensions").withStyle(ChatFormatting.RED), true);
                            }
                        } else {
                            System.out.println("Waypoint not found in player capability at position: " + message.pos);
                            waypoints.removeWaypoint(message.pos);
                            WaypointsProvider.sync(player);
                            player.displayClientMessage(Component.literal("Waypoint not found in player data").withStyle(ChatFormatting.RED), true);
                        }
                    });
                }
            } else {
                System.out.println("Player not found");
            }
        });

        context.setPacketHandled(true);
    }
}