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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class PostboxMenu extends AbstractContainerMenu {
//        private final PostboxBlockEntity = NULL;
        private final ContainerLevelAccess levelAccess;

        // Client Constructor
        public PostboxMenu(int containerId, Inventory playerInv) {
            this(containerId, playerInv, ContainerLevelAccess.NULL);
        }

        // Server Constructor
        public PostboxMenu(int containerId, Inventory playerInv, ContainerLevelAccess access) {
            super(MenuTypeInit.POSTBOX_MENU.get(), containerId);
            this.levelAccess = access;

            createPlayerHotbar(playerInv);
            createPlayerInventory(playerInv);
//            createBlockEntityInventory(be);
        }

    private void createBlockEntityInventory(PostboxBlockEntity be) {
            be.getOptional().ifPresent(inventory -> {
                for (int row = 0; row < 3; row++) {
                    for (int column = 0; column < 9; column++) {
                        addSlot(new SlotItemHandler((IItemHandler) inventory,
                                column + (row * 9),
                                8 + (column * 18),
                                18 + (row * 18)));
                    }
                }
            });
        }

        private void createPlayerInventory(Inventory playerInv) {
            for (int row = 0; row < 3; row++) {
                for (int column = 0; column < 9; column++) {
                    addSlot(new Slot(playerInv,
                            9 + column + (row * 9),
                            8 + (column * 18),
                            84 + (row * 18)));
                }
            }
        }

        private void createPlayerHotbar(Inventory playerInv) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInv,
                        column,
                        8 + (column * 18),
                        142));
            }
        }


        @Override
        public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
            Slot fromSlot = getSlot(pIndex);
            ItemStack fromStack = fromSlot.getItem();

            if(fromStack.getCount() <= 0)
                fromSlot.set(ItemStack.EMPTY);

            if(!fromSlot.hasItem())
                return ItemStack.EMPTY;

            ItemStack copyFromStack = fromStack.copy();

            if(pIndex < 36) {
                // We are inside of the player's inventory
                if(!moveItemStackTo(fromStack, 36, 63, false))
                    return ItemStack.EMPTY;
            } else if (pIndex < 63) {
                // We are inside of the block entity inventory
                if(!moveItemStackTo(fromStack, 0, 36, false))
                    return ItemStack.EMPTY;
            } else {
                System.err.println("Invalid slot index: " + pIndex);
                return ItemStack.EMPTY;
            }

            fromSlot.setChanged();
            fromSlot.onTake(pPlayer, fromStack);

            return copyFromStack;
        }

        @Override
        public boolean stillValid(@NotNull Player pPlayer) {
            return stillValid(this.levelAccess, pPlayer, BlockInit.POSTBOX_BLOCK.get());
        }

//        public PostboxBlockEntity getBlockEntity() {
//            return this.blockEntity;
//        }
    }

