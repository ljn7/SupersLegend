package com.superworldsun.superslegend.items.item.songs;

import com.superworldsun.superslegend.registries.OcarinaSongInit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

public class SongOfHealingSheet extends SongSheetItem
{
    public SongOfHealingSheet()
    {
        super(OcarinaSongInit.SONG_OF_HEALING);
    }

    @Override
    protected void addSongDescription(List<Component> list) {
        list.add(Component.literal("Playing this will cure the weak")
                .withStyle(ChatFormatting.GRAY));
        list.add(Component.literal("and destroy curses")
                .withStyle(ChatFormatting.GRAY));
    }
}