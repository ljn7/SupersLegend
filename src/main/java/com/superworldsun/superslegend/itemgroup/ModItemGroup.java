package com.superworldsun.superslegend.itemgroup;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class ModItemGroup {
    public static CreativeModeTab create(String name, RegistryObject<? extends Item> iconItem) {
        return CreativeModeTab.builder()
                .title(Component.translatable("itemGroup." + name))
                .icon(() -> new ItemStack(iconItem.get()))
                .build();
    }
}