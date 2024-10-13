package com.superworldsun.superslegend.container.inventory;

import com.superworldsun.superslegend.items.bags.BagItem;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BagInventory implements Container {
    private final NonNullList<ItemStack> stacks;
    private final ItemStack bag;

    private BagInventory(ItemStack bag, int size) {
        this.bag = bag;
        this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
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
        return stacks.stream().allMatch(ItemStack::isEmpty);
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
        saveToStack(bag);
    }

    @Override
    public boolean canPlaceItem(int slotIndex, ItemStack itemStack) {
        return bag.getItem() instanceof BagItem && ((BagItem) bag.getItem()).canHoldItem(itemStack);
    }

    @Override
    public boolean stillValid(Player player) {
        return player.isAlive();
    }

    public void load(CompoundTag nbt) {
        ListTag stackTags = nbt.getList("Slots", Tag.TAG_COMPOUND);
        for (int i = 0; i < stackTags.size(); i++) {
            stacks.set(i, ItemStack.of((CompoundTag) stackTags.get(i)));
        }
    }

    public CompoundTag save(CompoundTag nbt) {
        ListTag stackTags = new ListTag();
        stacks.forEach(stack -> stackTags.add(stack.save(new CompoundTag())));
        nbt.put("Slots", stackTags);
        return nbt;
    }

    public static BagInventory fromStack(ItemStack stack, int size) {
        BagInventory inventory = new BagInventory(stack, size);
        inventory.load(stack.getOrCreateTag().getCompound("Inventory"));
        return inventory;
    }

    public void saveToStack(ItemStack stack) {
        stack.getOrCreateTag().put("Inventory", save(new CompoundTag()));
    }
}