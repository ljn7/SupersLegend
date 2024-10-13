package com.superworldsun.superslegend.container.bag;

import com.superworldsun.superslegend.registries.ContainerInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;

public class BigRingBoxContainer extends BagContainer {
    public BigRingBoxContainer(int containerId, Inventory playerInventory, InteractionHand activeHand) {
        super(ContainerInit.RING_BOX_BIG.get(), containerId, playerInventory, activeHand, 2, 9);
    }

    public BigRingBoxContainer(int containerId, Inventory playerInventory, FriendlyByteBuf additionalData) {
        this(containerId, playerInventory, additionalData.readEnum(InteractionHand.class));
    }
}