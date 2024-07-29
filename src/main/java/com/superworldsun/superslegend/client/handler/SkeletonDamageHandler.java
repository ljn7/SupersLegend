package com.superworldsun.superslegend.client.handler;

import com.superworldsun.superslegend.interfaces.TameableEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SkeletonDamageHandler {
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        Entity source = event.getSource().getEntity();
        if (source instanceof AbstractSkeleton && event.getEntity() instanceof LivingEntity) {
            AbstractSkeleton skeleton = (AbstractSkeleton) source;
            LivingEntity target = event.getEntity();

            // Check if the attacker is a TameableEntity
            if (skeleton instanceof TameableEntity) {
                TameableEntity tameableSkeleton = (TameableEntity) skeleton;
                LivingEntity owner = tameableSkeleton.getOwner().orElse(null);

                // Prevent the owner from being damaged by their tamed entity
                if (owner != null && target.equals(owner)) {
                    event.setCanceled(true);
                    return;
                }

                // Prevent allied entities from damaging each other
                if (target instanceof TameableEntity) {
                    TameableEntity tameableTarget = (TameableEntity) target;

                    if (tameableSkeleton.hasOwner() && tameableTarget.hasOwner()) {
                        if (tameableSkeleton.getOwnerUniqueId().equals(tameableTarget.getOwnerUniqueId())) {
                            event.setCanceled(true);
                        }
                    }
                } else if (target instanceof Wolf) {
                    Wolf wolfTarget = (Wolf) target;

                    if (wolfTarget.isTame() &&  tameableSkeleton.getOwnerUniqueId().equals(wolfTarget.getOwnerUUID())) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
