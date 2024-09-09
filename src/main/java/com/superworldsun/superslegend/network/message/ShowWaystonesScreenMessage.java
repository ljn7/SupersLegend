package com.superworldsun.superslegend.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShowWaystonesScreenMessage {
    public ShowWaystonesScreenMessage() {}

    public static ShowWaystonesScreenMessage decode(FriendlyByteBuf buf) {
        return new ShowWaystonesScreenMessage();
    }

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(ShowWaystonesScreenMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClientPacket())
        );
        ctx.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClientPacket() {
        // TODO WaypointsScreen
//        Minecraft.getInstance().setScreen(new WaypointsScreen());
    }
}