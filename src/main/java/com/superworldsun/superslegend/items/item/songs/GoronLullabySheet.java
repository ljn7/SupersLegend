package com.superworldsun.superslegend.items.item.songs;

import java.util.List;

import com.superworldsun.superslegend.registries.OcarinaSongInit;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class GoronLullabySheet extends SongSheetItem
{
	public GoronLullabySheet()
	{
		super(OcarinaSongInit.GORON_LULLABY);
	}
	
	@Override
	protected void addSongDescription(List<Component> list)
	{
		list.add(Component.literal("Playing this will calm the angry").withStyle(ChatFormatting.GRAY));
	}
}
