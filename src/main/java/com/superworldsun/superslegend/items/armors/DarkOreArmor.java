package com.superworldsun.superslegend.items.armors;

import com.superworldsun.superslegend.items.customclass.NonEnchantArmor;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class DarkOreArmor extends NonEnchantArmor implements GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public DarkOreArmor(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    private PlayState predicate(AnimationState animationState){
        animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
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

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (player.tickCount % 20 == 0) { // Check if 20 ticks have passed
            if (level.isDay()) {
                    if (level.canSeeSky(player.blockPosition())) {
                        stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(getEquipmentSlot()));
                    }
            }
        }
    }
}
