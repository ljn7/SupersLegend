package com.superworldsun.superslegend.songs;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.network.NetworkDispatcher;
import com.superworldsun.superslegend.network.message.SyncLearnedSongsMessage;
import com.superworldsun.superslegend.registries.OcarinaSongInit;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LearnedSongs implements INBTSerializable<CompoundTag> {
    private static final String NBT_LEARNED_SONGS = "LearnedSongs";
    private static final String NBT_CURRENT_SONG = "CurrentSong";

    private final Set<OcarinaSong> learnedSongs = new HashSet<>();
    private OcarinaSong currentSong;

    public Set<OcarinaSong> getLearnedSongs() {
        return learnedSongs;
    }

    public OcarinaSong getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(OcarinaSong song) {
        this.currentSong = song;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        if (!learnedSongs.isEmpty()) {
            ListTag learnedSongsList = new ListTag();
            for (OcarinaSong song : learnedSongs) {
                learnedSongsList.add(StringTag.valueOf(song.getRegistryName().toString()));
            }
            nbt.put(NBT_LEARNED_SONGS, learnedSongsList);
        } else {
            nbt.put(NBT_LEARNED_SONGS, new ListTag());
        }
        if (currentSong != null) {
            nbt.putString(NBT_CURRENT_SONG, currentSong.getRegistryName().toString());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        learnedSongs.clear();
        ListTag songList = nbt.getList(NBT_LEARNED_SONGS, 8); // 8 is the ID for StringTag
        for (int i = 0; i < songList.size(); i++) {
            ResourceLocation songId = new ResourceLocation(songList.getString(i));
            OcarinaSong song = OcarinaSongInit.OCARINA_SONGS.getEntries().stream()
                    .filter(entry -> entry.getId().equals(songId))
                    .findFirst()
                    .map(RegistryObject::get)
                    .orElse(null);
            if (song != null) {
                learnedSongs.add(song);
            }
        }
        if (nbt.contains(NBT_CURRENT_SONG)) {
            ResourceLocation currentSongId = new ResourceLocation(nbt.getString(NBT_CURRENT_SONG));
            currentSong = OcarinaSongInit.OCARINA_SONGS.getEntries().stream()
                    .filter(entry -> entry.getId().equals(currentSongId))
                    .findFirst()
                    .map(RegistryObject::get)
                    .orElse(null);
        }
    }

    public static class Provider {
        public static final String LEARNED_SONGS_KEY = SupersLegendMain.MOD_ID + ":learned_songs";

        public static LearnedSongs getLearnedSongs(Player player) {
            CompoundTag persistentData = player.getPersistentData();
            LearnedSongs learnedSongs = new LearnedSongs();;
            if (persistentData.contains(LEARNED_SONGS_KEY)) {
                learnedSongs.deserializeNBT(persistentData.getCompound(LEARNED_SONGS_KEY));
            }
            return learnedSongs;
        }

        public static void saveLearnedSongs(Player player, LearnedSongs learnedSongs) {
            player.getPersistentData().put(LEARNED_SONGS_KEY, learnedSongs.serializeNBT());
        }

        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.Clone event) {
            Player original = event.getOriginal();
            Player player = event.getEntity();
            LearnedSongs originalPlayerSongs = getLearnedSongs(original);
            saveLearnedSongs(player, originalPlayerSongs);
        }

        @SubscribeEvent
        public static void onPlayerJoinWorld(PlayerEvent.PlayerLoggedInEvent event) {
            Player player = event.getEntity();
            LearnedSongs learnedSongs = getLearnedSongs(player);
            saveLearnedSongs(player, learnedSongs);
            if (player instanceof ServerPlayer) {
                sync((ServerPlayer) player);
            }
        }

        public static void sync(ServerPlayer player) {
            LearnedSongs learnedSongs = getLearnedSongs(player);
            NetworkDispatcher.network_channel.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncLearnedSongsMessage(learnedSongs.serializeNBT())
            );
        }
    }
}