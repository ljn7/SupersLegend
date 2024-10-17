package com.superworldsun.superslegend.container.bag;

import com.superworldsun.superslegend.registries.ContainerInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;

public class BiggestRingBoxContainer extends BagContainer {
    public BiggestRingBoxContainer(int containerId, Inventory playerInventory, InteractionHand activeHand) {
        super(ContainerInit.RING_BOX_BIGGEST.get(), containerId, playerInventory, activeHand, 3, 9);
    }

    public BiggestRingBoxContainer(int containerId, Inventory playerInventory, FriendlyByteBuf additionalData) {
        this(containerId, playerInventory, additionalData.readEnum(InteractionHand.class));
    }
}
