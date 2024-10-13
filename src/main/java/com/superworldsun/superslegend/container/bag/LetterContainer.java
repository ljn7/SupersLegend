package com.superworldsun.superslegend.container.bag;

import com.superworldsun.superslegend.registries.ContainerInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;

public class LetterContainer extends BagContainer {
    public LetterContainer(int containerId, Inventory playerInventory, InteractionHand activeHand) {
        super(ContainerInit.LETTER.get(), containerId, playerInventory, activeHand, 1, 1);
    }

    public LetterContainer(int containerId, Inventory playerInventory, FriendlyByteBuf additionalData) {
        this(containerId, playerInventory, additionalData.readEnum(InteractionHand.class));
    }
}