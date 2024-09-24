package com.superworldsun.superslegend.network.message;

import com.superworldsun.superslegend.blocks.entity.GossipStoneBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetGossipStoneTextMessage {
    private BlockPos pos;
    private String text;

    public SetGossipStoneTextMessage(BlockPos pos, String text) {
        this.pos = pos;
        this.text = text;
    }

    private SetGossipStoneTextMessage() {
    }

    public static SetGossipStoneTextMessage decode(FriendlyByteBuf buf) {
        SetGossipStoneTextMessage result = new SetGossipStoneTextMessage();
        result.pos = buf.readBlockPos();
        result.text = buf.readUtf();
        return result;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(text);
    }

    public static void handle(SetGossipStoneTextMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ServerPlayer player = ctx.getSender();
        ctx.enqueueWork(() -> {
            if (player != null && player.level().getBlockEntity(message.pos) instanceof GossipStoneBlockEntity gossipStone) {
                gossipStone.setMessage(message.text);
            }
        });
        ctx.setPacketHandled(true);
    }
}