package com.superworldsun.superslegend.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShowWaystoneCreationScreenMessage {
    private final BlockPos blockPos;

    public ShowWaystoneCreationScreenMessage(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public static ShowWaystoneCreationScreenMessage decode(FriendlyByteBuf buf) {
        return new ShowWaystoneCreationScreenMessage(buf.readBlockPos());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
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
        // TODO Waypoints Creation Screen
        //        Minecraft.getInstance().setScreen(new WaypointCreationScreen(message.blockPos));
    }
}
