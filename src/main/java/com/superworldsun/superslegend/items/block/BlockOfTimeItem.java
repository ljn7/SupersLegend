package com.superworldsun.superslegend.items.block;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class BlockOfTimeItem extends BlockItem {

    public BlockOfTimeItem(Block block)
    {
        super(block, new Properties().stacksTo(64));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag)
    {
        super.appendHoverText(stack, world, list, flag);
        list.add(Component.literal("can be displaced & returned to their").withStyle(ChatFormatting.BLUE));
        list.add(Component.literal("original location by playing the \"Song of Time\"").withStyle(ChatFormatting.BLUE));
    }

}
