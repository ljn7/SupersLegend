package com.superworldsun.superslegend.registries;

import com.superworldsun.superslegend.itemgroup.ModItemGroup;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class ItemGroupInit {
    public static final CreativeModeTab RESOURCES = ModItemGroup.create("supers_legend", ItemInit.TRIFORCE);
    public static final CreativeModeTab APPAREL = ModItemGroup.create("supers_legend_apparel", ItemInit.KOKIRI_TUNIC);
    public static final CreativeModeTab BLOCKS = ModItemGroup.create("supers_legend_blocks",
            BlockInit.getBlockItem("block_of_time"));
}