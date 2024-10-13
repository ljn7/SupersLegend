package com.superworldsun.superslegend.items.bags;

import com.superworldsun.superslegend.container.bag.LetterContainer;
import com.superworldsun.superslegend.container.bag.RingBoxContainer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class LetterItem extends BagItem
{
    public LetterItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public AbstractContainerMenu createContainer(int containerId, Inventory playerInventory, Player player, InteractionHand hand) {
        try {
            return new LetterContainer(containerId, playerInventory, hand);
        } catch (Exception e) {
            System.err.println("Error creating LetterContainer: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean canHoldItem(ItemStack stack)
    {
        return true;
    }
}
