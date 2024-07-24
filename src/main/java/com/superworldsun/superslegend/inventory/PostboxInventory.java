package com.superworldsun.superslegend.inventory;

import com.superworldsun.superslegend.blocks.entity.PostboxBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Optional;

public class PostboxInventory extends ItemStackHandler {
	private final PostboxBlockEntity postbox;

	public PostboxInventory(PostboxBlockEntity postbox) {
		super(9);
		this.postbox = postbox;
	}

	@Override
	protected void onContentsChanged(int slot) {
		super.onContentsChanged(slot);
		postbox.setChanged();
	}

	public void load(CompoundTag nbt) {
		ListTag stackTags = nbt.getList("Slots", CompoundTag.TAG_COMPOUND);
		for (int i = 0; i < stackTags.size(); i++) {
			setStackInSlot(i, ItemStack.of(stackTags.getCompound(i)));
		}
	}

	public CompoundTag save(CompoundTag nbt) {
		ListTag stackTags = new ListTag();
		for (int i = 0; i < getSlots(); i++) {
			ItemStack stack = getStackInSlot(i);
			if (!stack.isEmpty()) {
				stackTags.add(stack.save(new CompoundTag()));
			}
		}
		nbt.put("Slots", stackTags);
		return nbt;
	}

}
