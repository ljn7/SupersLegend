package com.superworldsun.superslegend.items.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.registries.RegistryObject;

public class ModHiddenBlockItem extends BlockItem {
    public ModHiddenBlockItem(RegistryObject<Block> blockObject) {
        super(blockObject.get(), new Item.Properties().stacksTo(64));
    }
}
