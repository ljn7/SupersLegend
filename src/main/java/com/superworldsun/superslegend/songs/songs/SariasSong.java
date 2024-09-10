package com.superworldsun.superslegend.songs.songs;

import com.superworldsun.superslegend.registries.SoundInit;
import com.superworldsun.superslegend.songs.OcarinaSong;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SariasSong extends OcarinaSong {
	public SariasSong() {
		super("drldrl", 0x43BE60);
	}

	@Override
	public SoundEvent getPlayingSound() {
		return SoundInit.SARIAS_SONG.get();
	}

	@Override
	public boolean requiresOcarinaOfTime() {
		return false;
	}

	@Override
	public void onSongPlayed(Player player, Level level) {
	}
}
