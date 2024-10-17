package com.superworldsun.superslegend.items.bags;

import com.superworldsun.superslegend.registries.ItemInit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class SpoilsBagItem extends BagItem
{
    public SpoilsBagItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean canHoldItem(ItemStack stack)
    {
        Item item = stack.getItem();
        return item == Items.GUNPOWDER || item == Items.ROTTEN_FLESH || item == Items.BONE || item == Items.BLAZE_ROD
                || item == Items.PRISMARINE_SHARD || item == Items.PRISMARINE_CRYSTALS || item == Items.ENDER_PEARL
                || item == Items.MAGMA_CREAM || item == Items.PHANTOM_MEMBRANE || item == Items.SHULKER_SHELL
                || item == Items.SLIME_BALL || item == Items.SPIDER_EYE || item == Items.STRING || item == Items.GHAST_TEAR
                || item == ItemInit.RED_JELLY.get() || item == ItemInit.GREEN_JELLY.get() || item == ItemInit.BLUE_JELLY.get()
                || item == ItemInit.TRIFORCE_COURAGE_SHARD.get() || item == ItemInit.TRIFORCE_WISDOM_SHARD.get()
                || item == ItemInit.TRIFORCE_POWER_SHARD.get();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag)
    {
        super.appendHoverText(stack, world, list, flag);
        list.add(Component.literal("Holds your spoils").withStyle(ChatFormatting.DARK_PURPLE));
    }
}