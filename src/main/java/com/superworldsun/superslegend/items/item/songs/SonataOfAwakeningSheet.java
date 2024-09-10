package com.superworldsun.superslegend.items.item.songs;

import java.util.List;

import com.superworldsun.superslegend.registries.OcarinaSongInit;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class SonataOfAwakeningSheet extends SongSheetItem
{
	public SonataOfAwakeningSheet()
	{
		super(OcarinaSongInit.SONATA_OF_AWAKENING);
	}
	
	@Override
	protected void addSongDescription(List<Component> list)
	{
		list.add(Component.literal("Playing this will wake the heaviest of sleepers").withStyle(ChatFormatting.GRAY));
	}
}
