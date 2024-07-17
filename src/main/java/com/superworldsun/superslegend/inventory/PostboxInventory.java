package com.superworldsun.superslegend.inventory;

import com.superworldsun.superslegend.blocks.entity.PostboxBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class PostboxInventory implements Container {
	private final NonNullList<ItemStack> stacks = NonNullList.withSize(9, ItemStack.EMPTY);
	private final PostboxBlockEntity postbox;

	public PostboxInventory(PostboxBlockEntity postbox) {
		this.postbox = postbox;
	}

	@Override
	public void clearContent() {
		stacks.clear();
		setChanged();
	}

	@Override
	public int getMaxStackSize() {
		return 64;
	}

	@Override
	public int getContainerSize() {
		return stacks.size();
	}

	@Override
	public boolean isEmpty() {
		return stacks.isEmpty();
	}

	@Override
	public ItemStack getItem(int index) {
		return stacks.get(index);
	}

	@Override
	public ItemStack removeItem(int index, int amount) {
		ItemStack itemstack = ContainerHelper.removeItem(stacks, index, amount);
		if (!itemstack.isEmpty()) {
			this.setChanged();
		}
		return itemstack;
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		ItemStack stack = stacks.get(index);
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			stacks.set(index, ItemStack.EMPTY);
			return stack;
		}
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		stacks.set(index, stack);
		if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}
		setChanged();
	}

	@Override
	public void setChanged() {
		Optional.ofNullable(postbox).ifPresent(PostboxBlockEntity::setChanged);
	}

	@Override
	public boolean stillValid(Player player) {
		return player.isAlive() && postbox.getLevel().getBlockEntity(postbox.getBlockPos()) == postbox;
	}

	public void load(CompoundTag nbt) {
		ListTag stackTags = nbt.getList("Slots", CompoundTag.TAG_COMPOUND);
		for (int i = 0; i < stackTags.size(); i++) {
			stacks.set(i, ItemStack.of(stackTags.getCompound(i)));
		}
	}

	public CompoundTag save(CompoundTag nbt) {
		ListTag stackTags = new ListTag();
		stacks.forEach(stack -> stackTags.add(stack.save(new CompoundTag())));
		nbt.put("Slots", stackTags);
		return nbt;
	}
}
