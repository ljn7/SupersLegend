package com.superworldsun.superslegend.container.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BagSlot extends Slot {
    public BagSlot(Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return container.canPlaceItem(getSlotIndex(), itemStack);
    }
}
