package com.superworldsun.superslegend.items.item.songs;

import com.superworldsun.superslegend.registries.OcarinaSongInit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ZeldasLullabySheet extends SongSheetItem
{
    public ZeldasLullabySheet()
    {
        super(OcarinaSongInit.ZELDAS_LULLABY);
    }

    @Override
    protected void addSongDescription(List<Component> list) {
        list.add(Component.literal("Playing this will activate nearby Royal Tiles")
                .withStyle(ChatFormatting.GRAY));
    }
}