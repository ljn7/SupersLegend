package com.superworldsun.superslegend.items.bags;

import com.superworldsun.superslegend.container.bag.RingBoxContainer;
import com.superworldsun.superslegend.items.customclass.RingItem;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypePreset;

public class RingBoxItem extends BagItem {
    public RingBoxItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractContainerMenu createContainer(int containerId, Inventory playerInventory, Player player, InteractionHand hand) {
        try {
            return new RingBoxContainer(containerId, playerInventory, hand);
        } catch (Exception e) {
            System.err.println("Error creating RingBoxContainer: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        return CuriosApi.getCuriosHelper().getCurioTags(stack.getItem()).contains(SlotTypePreset.RING.getIdentifier());
    }
}