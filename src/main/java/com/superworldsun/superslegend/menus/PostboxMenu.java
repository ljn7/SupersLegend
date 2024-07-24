package com.superworldsun.superslegend.menus;

import com.superworldsun.superslegend.blocks.entity.PostboxBlockEntity;
import com.superworldsun.superslegend.registries.BlockInit;
import com.superworldsun.superslegend.registries.MenuTypeInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class PostboxMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess levelAccess;

    // Client Constructor
    public PostboxMenu(int containerId, Inventory playerInv, FriendlyByteBuf friendlyByteBuf) {
        this(containerId, playerInv, playerInv.player.level().getBlockEntity(friendlyByteBuf.readBlockPos()));
    }

    // Server Constructor
    public PostboxMenu(int containerId, Inventory playerInv, BlockEntity blockEntity) {
        super(MenuTypeInit.POSTBOX_MENU.get(), containerId);
        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        createPlayerHotbar(playerInv);
        createPlayerInventory(playerInv);
        createBlockEntityInventory((PostboxBlockEntity) blockEntity);
    }


    private void createBlockEntityInventory(PostboxBlockEntity be) {
        be.getOptional().ifPresent(inventory -> {
            // Add the 3x3 inventory slots
            int slotIndex = 0;
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    addSlot(new SlotItemHandler(inventory, slotIndex++, 62 + x * 18, 18 + y * 18));
                }
            }
        });
    }

    private void createPlayerInventory(Inventory playerInv) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 86 + y * 18));
            }
        }
    }

    private void createPlayerHotbar(Inventory playerInv) {
        for (int x = 0; x < 9; x++) {
            addSlot(new Slot(playerInv, x, 8 + x * 18, 144));
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.levelAccess, player, BlockInit.POSTBOX_BLOCK.get());
    }

    @Override
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 9) { // Assuming the first 9 slots are for PostboxInventory
                if (!this.moveItemStackTo(itemstack1, 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(itemstack1, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

}
