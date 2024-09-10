package com.superworldsun.superslegend.items.item.songs;

import java.util.List;

import com.superworldsun.superslegend.registries.OcarinaSongInit;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class SongOfTimeSheet extends SongSheetItem
{
	public SongOfTimeSheet()
	{
		super(OcarinaSongInit.SONG_OF_TIME);
	}
	
	@Override
	protected void addSongDescription(List<Component> list)
	{
		list.add(Component.literal("Playing this will toggle Blocks of Time").withStyle(ChatFormatting.GRAY));
	}
}
