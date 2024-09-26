package com.superworldsun.superslegend.network.message;

import com.superworldsun.superslegend.client.screen.WaypointCreationScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ShowWaystoneCreationScreenMessage {
    private final BlockPos blockPos;
    private final UUID playerUUID;

    public ShowWaystoneCreationScreenMessage(BlockPos blockPos, UUID playerUUID) {
        this.blockPos = blockPos;
        this.playerUUID = playerUUID;
    }

    public static ShowWaystoneCreationScreenMessage decode(FriendlyByteBuf buf) {
        return new ShowWaystoneCreationScreenMessage(buf.readBlockPos(), buf.readUUID());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeUUID(playerUUID);
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
            Minecraft.getInstance().setScreen(new WaypointCreationScreen(message.blockPos, player));
        }
    }
}
