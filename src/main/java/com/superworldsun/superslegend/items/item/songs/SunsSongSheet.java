package com.superworldsun.superslegend.items.item.songs;

import java.util.List;

import com.superworldsun.superslegend.registries.OcarinaSongInit;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class SunsSongSheet extends SongSheetItem
{
	public SunsSongSheet()
	{
		super(OcarinaSongInit.SUNS_SONG);
	}
	
	@Override
	protected void addSongDescription(List<Component> list)
	{
		list.add(Component.literal("Playing this will accelerate the passage")
				.withStyle(ChatFormatting.GRAY));
		list.add(Component.literal("of time to Day and Night")
				.withStyle(ChatFormatting.GRAY));
	}
}
