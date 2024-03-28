package com.superworldsun.superslegend.items.weapons.boomerang;

import com.superworldsun.superslegend.entities.projectiles.boomerang.AbstractBoomerangEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractBoomerangItem extends Item {
    public AbstractBoomerangItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        AbstractBoomerangEntity boomerang = getProjectileConstructor().create(player, stack);
        level.addFreshEntity(boomerang);
        player.setItemInHand(hand, ItemStack.EMPTY);
        return InteractionResultHolder.success(ItemStack.EMPTY);
    }

    protected abstract BoomerangConsructor getProjectileConstructor();

    @FunctionalInterface
    protected interface BoomerangConsructor {
        AbstractBoomerangEntity create(Player owner, ItemStack stack);
    }
}
