package com.superworldsun.superslegend.client.sound;

import com.superworldsun.superslegend.songs.LearnedSongs;
import com.superworldsun.superslegend.songs.OcarinaSong;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

public class OcarinaSongSound extends AbstractTickableSoundInstance {

    private final Player player;
    private final OcarinaSong song;

    public OcarinaSongSound(Player entity, OcarinaSong song)
    {
        super(song.getPlayingSound(), SoundSource.PLAYERS, RandomSource.create());
        this.player = entity;
        this.delay = 0;
        this.volume = 1.0F;
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
        this.song = song;
    }

    @Override
    public void tick() {
        if (!player.isAlive()) {
            stop();
            return;
        }

        LearnedSongs learnedSongs = LearnedSongs.Provider.getLearnedSongs(player);
        if (learnedSongs.getCurrentSong() != song) {
            stop();
            return;
        }

        x = player.getX();
        y = player.getY();
        z = player.getZ();
    }

    @Override
    public boolean canPlaySound()
    {
        return !player.isSilent();
    }

    @Override
    public boolean canStartSilent()
    {
        return true;
    }
}