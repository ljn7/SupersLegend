package com.superworldsun.superslegend.songs.songs;

import com.superworldsun.superslegend.registries.SoundInit;
import com.superworldsun.superslegend.songs.OcarinaSong;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;


public class NewWaveBossaNova extends OcarinaSong
{
	public NewWaveBossaNova()
	{
		super("lulrdlr", 0x1D235F);
	}

	@Override
	public SoundEvent getPlayingSound()
	{
		return SoundInit.NEW_WAVE_BOSSA_NOVA.get();
	}

	@Override
	public void onSongPlayed(Player player, Level level)
	{

	}
}
