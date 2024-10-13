package com.superworldsun.superslegend.items.bags;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class BaitBagItem extends BagItem
{
    public BaitBagItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean canHoldItem(ItemStack stack)
    {
        Item item = stack.getItem();
        boolean isCrops = item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof CropBlock;
        boolean isFood = item.getFoodProperties() != null;
        return isCrops || isFood || item == Items.WHEAT;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag)
    {
        super.appendHoverText(stack, world, list, flag);
        list.add(Component.literal("Holds all types of parchment and letters").withStyle(ChatFormatting.GOLD));
    }
}
