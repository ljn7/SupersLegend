package com.superworldsun.superslegend.songs.songs;

import com.superworldsun.superslegend.registries.SoundInit;
import com.superworldsun.superslegend.songs.OcarinaSong;


import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;


public class OathToOrder extends OcarinaSong
{
	public OathToOrder()
	{
		super("rdadru", 0x842719);
	}

	@Override
	public SoundEvent getPlayingSound()
	{
		return SoundInit.OATH_TO_ORDER.get();
	}

	@Override
	public void onSongPlayed(Player player, Level level)
	{

	}
}
