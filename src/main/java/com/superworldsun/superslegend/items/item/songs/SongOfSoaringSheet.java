package com.superworldsun.superslegend.items.item.songs;

import java.util.List;

import com.superworldsun.superslegend.registries.OcarinaSongInit;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class SongOfSoaringSheet extends SongSheetItem
{
	public SongOfSoaringSheet()
	{
		super(OcarinaSongInit.SONG_OF_SOARING);
	}
	
	@Override
	protected void addSongDescription(List<Component> list)
	{
		list.add(Component.literal("Playing this will allow you to fly").withStyle(ChatFormatting.GRAY));
		list.add(Component.literal("to any activated owl statues").withStyle(ChatFormatting.GRAY));
	}
}
