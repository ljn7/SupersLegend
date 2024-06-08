package com.superworldsun.superslegend.items.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class DarkShovel extends ShovelItem {

    public DarkShovel(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, level, entity, itemSlot, isSelected);
        Player player = (Player) entity;
        if (player.tickCount % 20 == 0) { // Check if 20 ticks have passed
            if (level.isDay()) {
                if (entity instanceof Player && player.getMainHandItem().equals(stack) || player.getOffhandItem().equals(stack)) {
                    if (level.canSeeSky(player.blockPosition())) {
                        stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
                    }
                }
            }
        }
    }
}
