package com.superworldsun.superslegend.items.bags;

import com.superworldsun.superslegend.container.bag.BagContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public abstract class BagItem extends Item {
    public BagItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, createMenuProvider(player, hand), buf -> buf.writeEnum(hand));
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_ELYTRA, SoundSource.PLAYERS, 1f, 1f);
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    protected MenuProvider createMenuProvider(Player player, InteractionHand hand) {
        return new MenuProvider() {
            @Override
            public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
                return createContainer(containerId, playerInventory, player, hand);
            }

            @Override
            public Component getDisplayName() {
                return player.getItemInHand(hand).getHoverName();
            }
        };
    }

    public AbstractContainerMenu createContainer(int containerId, Inventory playerInventory, Player player, InteractionHand hand) {
        try {
            return new BagContainer(containerId, player.getInventory(), hand);
        } catch (Exception e) {
            System.err.println("Error creating BagContainer: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public abstract boolean canHoldItem(ItemStack stack);
}