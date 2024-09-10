package com.superworldsun.superslegend.items.item.songs;

import java.util.List;

import com.superworldsun.superslegend.registries.OcarinaSongInit;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class SongOfStormsSheet extends SongSheetItem
{
	public SongOfStormsSheet()
	{
		super(OcarinaSongInit.SONG_OF_STORMS);
	}
	
	@Override
	protected void addSongDescription(List<Component> list)
	{
		list.add(Component.literal("Playing this will summon a storm").withStyle(ChatFormatting.GRAY));
	}
}
