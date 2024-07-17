package com.superworldsun.superslegend.container;

import com.superworldsun.superslegend.inventory.PostboxInventory;
import com.superworldsun.superslegend.registries.ContainerInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class PostboxContainer extends SimpleContainer {
    public PostboxContainer(int windowId, Inventory playerInventory, PostboxInventory postboxInventory) {
        super(ContainerInit.POSTBOX.get(), windowId, playerInventory, postboxInventory, 3, 3);
    }

	public PostboxContainer(int windowId, Inventory playerInventory, FriendlyByteBuf additionalData) {
		this(windowId, playerInventory, new PostboxInventory(null));
	}
}