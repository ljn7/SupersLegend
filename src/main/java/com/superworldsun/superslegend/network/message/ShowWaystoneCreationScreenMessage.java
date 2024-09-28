package com.superworldsun.superslegend.network.message;

import com.superworldsun.superslegend.client.screen.WaypointCreationScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ShowWaystoneCreationScreenMessage {
    private final BlockPos blockPos;
    private final UUID playerUUID;
    private final Vec3 teleportPos;
    private final Direction facing;

    public ShowWaystoneCreationScreenMessage(BlockPos blockPos, Vec3 teleportPos, Direction facing, UUID playerUUID) {
        this.blockPos = blockPos;
        this.playerUUID = playerUUID;
        this.teleportPos = teleportPos;
        this.facing = facing;
    }

    public static ShowWaystoneCreationScreenMessage decode(FriendlyByteBuf buf) {
        BlockPos blockPos = buf.readBlockPos();
        UUID playerUUID = buf.readUUID();
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        Vec3 teleportPos = new Vec3(x, y, z);
        Direction facing = Direction.from3DDataValue(buf.readByte());
        return new ShowWaystoneCreationScreenMessage(blockPos, teleportPos, facing, playerUUID);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeUUID(playerUUID);
        buf.writeDouble(teleportPos.x);
        buf.writeDouble(teleportPos.y);
        buf.writeDouble(teleportPos.z);
        buf.writeByte(facing.get3DDataValue());
    }

    public static void handle(ShowWaystoneCreationScreenMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClientPacket(message))
        );
        ctx.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClientPacket(ShowWaystoneCreationScreenMessage message) {
        Minecraft minecraft = Minecraft.getInstance();
        UUID playerUUID = message.playerUUID;
        Player player = minecraft.level.getPlayerByUUID(playerUUID);
        if (player != null) {
            Minecraft.getInstance().setScreen(new WaypointCreationScreen(message.blockPos, message.teleportPos, message.facing, player));
        }
    }
}
