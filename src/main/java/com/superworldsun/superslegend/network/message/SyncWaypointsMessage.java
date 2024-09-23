package com.superworldsun.superslegend.network.message;

import com.superworldsun.superslegend.capability.waypoint.Waypoint;
import com.superworldsun.superslegend.capability.waypoint.Waypoints;
import com.superworldsun.superslegend.capability.waypoint.WaypointsProvider;
import com.superworldsun.superslegend.client.screen.WaypointsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SyncWaypointsMessage {
    private final CompoundTag nbt;

    public SyncWaypointsMessage(Player player) {
        this.nbt = player.getCapability(WaypointsProvider.WAYPOINTS_CAPABILITY)
                .map(Waypoints::serializeNBT)
                .orElse(new CompoundTag());
    }

    private SyncWaypointsMessage(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public static void encode(SyncWaypointsMessage message, FriendlyByteBuf buf) {
        buf.writeNbt(message.nbt);
    }

    public static SyncWaypointsMessage decode(FriendlyByteBuf buf) {
        return new SyncWaypointsMessage(buf.readNbt());
    }

    public static void handle(SyncWaypointsMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Make sure we're on the client side
            if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                ClientHandler.handlePacket(message);
            }
        });
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static class ClientHandler {
        static void handlePacket(SyncWaypointsMessage message) {
            Minecraft client = Minecraft.getInstance();
            if (client.player != null) {
                client.player.getCapability(WaypointsProvider.WAYPOINTS_CAPABILITY).ifPresent(waypoints -> {
                    ((Waypoints)waypoints).deserializeNBT(message.nbt);

                    if (client.screen instanceof WaypointsScreen) {
                        client.setScreen(new WaypointsScreen(client.player));
                    }
                });
            }
        }
    }
}