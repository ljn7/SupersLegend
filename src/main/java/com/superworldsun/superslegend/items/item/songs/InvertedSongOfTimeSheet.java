package com.superworldsun.superslegend.items.item.songs;

import java.util.List;

import com.superworldsun.superslegend.registries.OcarinaSongInit;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class InvertedSongOfTimeSheet extends SongSheetItem
{
	public InvertedSongOfTimeSheet()
	{
		super(OcarinaSongInit.INVERTED_SONG_OF_TIME);
	}
	
	@Override
	protected void addSongDescription(List<Component> list)
	{
		list.add(Component.literal("Playing this will slow the passage of time.").withStyle(ChatFormatting.GRAY));
		list.add(Component.literal("Play a second time to return it to normal").withStyle(ChatFormatting.GRAY));
		list.add(Component.literal("Only works in single player").withStyle(ChatFormatting.RED));
	}
}
