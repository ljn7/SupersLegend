package com.superworldsun.superslegend.entities.projectiles.arrows;

import com.superworldsun.superslegend.registries.EntityTypeInit;
import com.superworldsun.superslegend.registries.ItemInit;
import com.superworldsun.superslegend.registries.TagInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class SilverArrowEntity extends AbstractArrow
{
    public SilverArrowEntity(EntityType<? extends SilverArrowEntity> type, Level level)
    {
        super(type, level);
    }

    public SilverArrowEntity(net.minecraft.world.level.Level worldIn, LivingEntity shooter)
    {
        super(EntityTypeInit.SILVER_ARROW.get(), shooter, worldIn);
    }

    @Override
    public void onAddedToWorld()
    {
        super.onAddedToWorld();
        setBaseDamage(4.0D);
    }

    @Override
    public void tick() {
        if (!this.inGround) {
            this.level().addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D,
                    0.0D);
        }
        super.tick();
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(ItemInit.SILVER_ARROW.get());
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHitEntity(EntityHitResult result)
    {
        Entity hitEntity = result.getEntity();

        if (!(hitEntity instanceof LivingEntity livingEntity)) {
            super.onHitEntity(result);
            return;
        }

        double originalDamage = this.getBaseDamage();

        boolean isWeakToLight = hitEntity.getType().is(TagInit.WEAK_TO_LIGHT);

        if (isWeakToLight || livingEntity.getMobType() == MobType.UNDEAD) {
            this.setBaseDamage(originalDamage * 8);
        }

        super.onHitEntity(result);

        this.setBaseDamage(originalDamage);

        if (!this.level().isClientSide && this.getPierceLevel() <= 0) {
            livingEntity.setArrowCount(livingEntity.getArrowCount() - 1);
        }
    }
}
