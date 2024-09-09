package com.superworldsun.superslegend.network.message;

import com.superworldsun.superslegend.songs.LearnedSongs;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncLearnedSongsMessage {
    private CompoundTag compoundTag;

    public SyncLearnedSongsMessage() {}

    public SyncLearnedSongsMessage(CompoundTag tag) {
        compoundTag = tag;
    }

    public static SyncLearnedSongsMessage decode(FriendlyByteBuf buf) {
        SyncLearnedSongsMessage result = new SyncLearnedSongsMessage();
        result.compoundTag = buf.readAnySizeNbt();
        return result;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(compoundTag);
    }

    public static void receive(SyncLearnedSongsMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.setPacketHandled(true);
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClientPacket(message)));
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClientPacket(SyncLearnedSongsMessage message) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) {
            return;
        }
        LearnedSongs learnedSongs = LearnedSongs.Provider.getLearnedSongs(client.player);
        learnedSongs.deserializeNBT(message.compoundTag);
        LearnedSongs.Provider.saveLearnedSongs(client.player, learnedSongs);
    }
}