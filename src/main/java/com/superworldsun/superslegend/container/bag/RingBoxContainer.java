package com.superworldsun.superslegend.container.bag;

import com.superworldsun.superslegend.registries.ContainerInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class RingBoxContainer extends BagContainer {
    public RingBoxContainer(int containerId, Inventory playerInventory, InteractionHand activeHand) {
        super(ContainerInit.RING_BOX.get(), containerId, playerInventory, activeHand, 1, 9);
    }

    public RingBoxContainer(int containerId, Inventory playerInventory, FriendlyByteBuf additionalData) {
        this(containerId, playerInventory, additionalData.readEnum(InteractionHand.class));
    }
}
