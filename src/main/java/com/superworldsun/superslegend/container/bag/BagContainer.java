package com.superworldsun.superslegend.container.bag;

import com.superworldsun.superslegend.container.SimpleContainer;
import com.superworldsun.superslegend.container.inventory.BagInventory;
import com.superworldsun.superslegend.container.slot.BagSlot;
import com.superworldsun.superslegend.container.slot.LockedSlot;
import com.superworldsun.superslegend.container.slot.SlotFactory;
import com.superworldsun.superslegend.registries.ContainerInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

public class BagContainer extends SimpleContainer {
    private final InteractionHand handHoldingBag;

    public BagContainer(MenuType<? extends BagContainer> type, int containerId, Inventory playerInventory, InteractionHand handHoldingBag, int slotRows, int slotColumns) {
        super(type, containerId, playerInventory, BagContainer.createBagInventory(playerInventory, handHoldingBag, slotRows * slotColumns), slotRows, slotColumns);
        this.handHoldingBag = handHoldingBag;
    }

    public BagContainer(int containerId, Inventory playerInventory, InteractionHand handHoldingBag) {
        this(ContainerInit.BAG.get(), containerId, playerInventory, handHoldingBag, 3, 9);
    }

    public BagContainer(int containerId, Inventory playerInventory, FriendlyByteBuf additionalData) {
        this(containerId, playerInventory, additionalData.readEnum(InteractionHand.class));
    }

    @Override
    protected SlotFactory getContainerSlotFactory(int slotIndex) {
        return BagSlot::new;
    }

    @Override
    protected SlotFactory getHotbarSlotFactory(int slotIndex) {
        return isBagInSlot(slotIndex) ? LockedSlot::new : Slot::new;
    }

    private boolean isBagInSlot(int slotIndex) {
        return handHoldingBag == InteractionHand.MAIN_HAND && playerInventory.selected == slotIndex;
    }

    protected static Container createBagInventory(Inventory playerInventory, InteractionHand handHoldingBag, int containerSize) {
        return BagInventory.fromStack(((Player)playerInventory.player).getItemInHand(handHoldingBag), containerSize);
    }
}