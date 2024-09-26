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

public class OwlStatueItem extends BlockItem
{
    public OwlStatueItem(RegistryObject<Block> blockObject)
    {
        super(blockObject.get(), new Properties().stacksTo(64));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag)
    {
        super.appendHoverText(stack, world, list, flag);
        list.add(Component.literal("When placed down you will create the name of the owl statue").withStyle(ChatFormatting.WHITE));
        list.add(Component.literal("After learning the \"Song of Soaring\", you can warp to any Owl Statue that's activated").withStyle(ChatFormatting.WHITE));
    }
}