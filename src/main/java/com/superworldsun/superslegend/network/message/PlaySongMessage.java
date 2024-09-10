package com.superworldsun.superslegend.network.message;


import com.superworldsun.superslegend.registries.OcarinaSongInit;
import com.superworldsun.superslegend.songs.OcarinaSong;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class PlaySongMessage {
    private ResourceLocation songId;

    public PlaySongMessage(OcarinaSong song) {
        this.songId = song.getRegistryName();
        if (this.songId == null) {
            // Fallback in case getRegistryName() is not available or returns null
            for (RegistryObject<OcarinaSong> entry : OcarinaSongInit.OCARINA_SONGS.getEntries()) {
                if (entry.get() == song) {
                    this.songId = entry.getId();
                    break;
                }
            }
        }
        if (this.songId == null) {
            throw new IllegalArgumentException("Could not find registry name for song: " + song);
        }
    }

    private PlaySongMessage(ResourceLocation songId) {
        this.songId = songId;
    }

    public static PlaySongMessage decode(FriendlyByteBuf buf) {
        return new PlaySongMessage(buf.readResourceLocation());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(songId);
    }

    public static void handle(PlaySongMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                OcarinaSongInit.OCARINA_SONGS.getEntries().stream()
                        .filter(entry -> entry.getId().equals(message.songId))
                        .findFirst()
                        .ifPresent(songEntry -> songEntry.get().onSongPlayed(player, player.level()));
            }
        });
        ctx.setPacketHandled(true);
    }
}