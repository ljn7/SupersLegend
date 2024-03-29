package com.superworldsun.superslegend.entities.projectiles.seeds;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

public class SeedEntity extends AbstractArrow {
    public SeedEntity(EntityType<? extends SeedEntity> type, Level level)
    {
        super(type, level);
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        setBaseDamage(getDamage());
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        if (!isNoPhysics()) {
            Vec3 previousMovement = getDeltaMovement();
            setDeltaMovement(previousMovement.x, previousMovement.y - getMass(), previousMovement.z);
        }

        if (inGround) {
            this.discard();
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.BAMBOO_BREAK;
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);

        if (result.getEntity() instanceof LivingEntity target) {
            if (!level().isClientSide && getPierceLevel() <= 0) {
                target.setArrowCount(target.getArrowCount() - 1);
            }
        }
    }

    public void shoot(Vec3 direction, float speed, float spread) {
        super.shoot(direction.x, direction.y, direction.z, speed * getFlightSpeed(), spread);
    }

    public void setPos(Vector3d pos) {
        setPos(pos.x, pos.y, pos.z);
    }

    protected float getMass() {
        return 0.05f;
    }

    protected float getFlightSpeed() {
        return 1f;
    }

    protected float getDamage() {
        return 2f;
    }
}