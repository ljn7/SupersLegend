package com.superworldsun.superslegend.network.message;

import com.superworldsun.superslegend.capability.waypoint.WaypointsServerData;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SetWaypointNameMessage {
    private final BlockPos blockPos;
    private final String name;
    private final UUID playerUUID;
    private final Vec3 teleportPos;
    private final Direction facing;

    public SetWaypointNameMessage(BlockPos pos, Vec3 teleportPos, Direction facing, String name, UUID playerUUID) {
        this.blockPos = pos;
        this.name = name;
        this.playerUUID = playerUUID;
        this.teleportPos = teleportPos;
        this.facing = facing;
    }

    public static void encode(SetWaypointNameMessage message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.blockPos);
        buf.writeUtf(message.name);
        buf.writeUUID(message.playerUUID);
        buf.writeDouble(message.teleportPos.x);
        buf.writeDouble(message.teleportPos.y);
        buf.writeDouble(message.teleportPos.z);
        buf.writeByte(message.facing.get3DDataValue());
    }

    public static SetWaypointNameMessage decode(FriendlyByteBuf buf) {
        BlockPos blockPos = buf.readBlockPos();
        String text = buf.readUtf();
        UUID playerUUID = buf.readUUID();
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        Vec3 teleportPos = new Vec3(x, y, z);
        Direction facing = Direction.from3DDataValue(buf.readByte());
        return new SetWaypointNameMessage(blockPos, teleportPos, facing, text, playerUUID);
    }

    public static void handle(SetWaypointNameMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.setPacketHandled(true);
        context.enqueueWork(() -> {
            ServerLevel level = context.getSender().serverLevel();
            context.enqueueWork(() -> WaypointsServerData.get(level).createWaypoint(message.blockPos, message.teleportPos, message.facing, message.name, level.dimension().location().toString()));
        });

    }
}
