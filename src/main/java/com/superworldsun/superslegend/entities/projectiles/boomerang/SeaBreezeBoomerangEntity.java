package com.superworldsun.superslegend.entities.projectiles.boomerang;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.registries.EntityTypeInit;
import com.superworldsun.superslegend.registries.SoundInit;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;

public class SeaBreezeBoomerangEntity extends AbstractBoomerangEntity {

    public SeaBreezeBoomerangEntity(EntityType<SeaBreezeBoomerangEntity> type, Level level) {
        super(type, level);
        turnBackTimer = 30;
        turningBack = false;
        bounceFactor = 0.84999999999999998D;
    }

    public SeaBreezeBoomerangEntity(Player owner, ItemStack stack) {
        super(EntityTypeInit.SEA_BREEZE_BOOMERANG.get(), owner, stack);
    }

    @Override
    protected SoundEvent getFlyLoopSound() {
        return SoundInit.WW_BOOMERANG_FLY_LOOP.get();
    }

    @Override
    protected float getDamage() {
        return 1f;
    }

    @Override
    public void tick() {
        Player owner = getOwner();
        if (owner == null || owner.isDeadOrDying()) {
            kill();
            return;
        }

        if (tickCount % 4 == 0) {
            level().playSound(null, getX(), getY(), getZ(), getFlyLoopSound(), SoundSource.PLAYERS, 1f, 1f);
        }

        updateCollision();
        if (!turningBack)
            updateGaleMotion();
        determineRotation();
        prevRotation = getRotation();
        setRotation(Mth.wrapDegrees(getRotation() + 36F));
        updateCarriedItems();
        super.tick();
    }

    private void updateGaleMotion() {
        Player owner = getOwner();
        if (owner == null) return;

        Vec3 lookAngle = owner.getLookAngle();
        double motionX = lookAngle.x * getDefaultSpeed;
        double motionY = lookAngle.y * getDefaultSpeed;
        double motionZ = lookAngle.z * getDefaultSpeed;

        setDeltaMovement(motionX, motionY, motionZ);

        double xPos = getX() + getDeltaMovement().x;
        double yPos = getY() + getDeltaMovement().y;
        double zPos = getZ() + getDeltaMovement().z;
        setPos(xPos, yPos, zPos);

        double distanceToOwner = distanceTo(owner);
        if (distanceToOwner < 1.5D) {
            onReturnToOwner();
        }
    }

    @Override
    public void onEntityHit(Entity entity) {
        Player owner = getOwner();
        if (entity instanceof ItemEntity item) {
            pickedItems.add(item);
        } else if (entity instanceof LivingEntity && entity != owner) {
            entity.hurt(getDamageSource(owner), getDamage());
        }
    }

    public static EntityType<SeaBreezeBoomerangEntity> createEntityType() {
        return EntityType.Builder.<SeaBreezeBoomerangEntity>of(SeaBreezeBoomerangEntity::new, MobCategory.MISC)
                .sized(0.5f, 0.5f)
                .build(SupersLegendMain.MOD_ID + ":sea_breeze_boomerang");
    }
}