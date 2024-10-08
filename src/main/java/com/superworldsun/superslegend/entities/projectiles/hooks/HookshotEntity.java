package com.superworldsun.superslegend.entities.projectiles.hooks;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.capability.hookshot.HookModel;
import com.superworldsun.superslegend.items.hookshot.HookshotItem;
import com.superworldsun.superslegend.registries.EntityTypeInit;
import com.superworldsun.superslegend.registries.SoundInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;

import static com.superworldsun.superslegend.items.hookshot.HookshotItem.SPRITE;
import static com.superworldsun.superslegend.util.HookBlockList.hookableBlocks;
import static com.superworldsun.superslegend.util.HookBlockList.setHookableBlocks;

import javax.annotation.Nonnull;
import java.util.List;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HookshotEntity extends AbstractArrow {

    private static final EntityDataAccessor<Integer> HOOKED_ENTITY_ID = SynchedEntityData.defineId(HookshotEntity.class, EntityDataSerializers.INT);
    boolean useBlockList = true;
    private double maxRange = 0D;
    private double maxSpeed = 0D;
    private boolean isPulling = false;
    private Player owner;
    private Entity hookedEntity;
    private ItemStack stack;
    private boolean motionUp = false;
    private double prevDistance = 30.D;

    public HookshotEntity(EntityType<? extends AbstractArrow> type, LivingEntity owner, Level world) {
        super(type, owner, world);
        this.setSoundEvent(SoundInit.HOOKSHOT_TARGET.get());
        this.setNoGravity(true);
        this.setBaseDamage(0);
    }

    public HookshotEntity(EntityType<HookshotEntity> hookshotEntityEntityType, Level world) {
        super(EntityTypeInit.HOOKSHOT_ENTITY.get(), world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HOOKED_ENTITY_ID, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount % 3 == 0) {
            BlockPos currentPos = this.blockPosition();
            this.level().playSound(null, currentPos, SoundInit.HOOKSHOT_EXTENDED.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        }

        if (getOwner() instanceof Player) {
            owner = (Player) getOwner();

            if (isPulling && tickCount % 2 == 0) {
                // Uncomment if you want to play a sound while pulling
                // level.playSound(null, owner.blockPosition(), SoundEvents.AXE_STRIP, SoundCategory.PLAYERS, 1F, 1F);
            }

            if (!level().isClientSide) {
                if (this.hookedEntity != null) {
                    if (isAlive()) {
                        this.hookedEntity = null;
                        onRemovedFromWorld();
                    } else {
                        this.absMoveTo(this.hookedEntity.getX(), this.hookedEntity.getY(0.8D), this.hookedEntity.getZ());
                    }
                }

                if (owner != null) {
                    if (owner.isDeadOrDying() || tickCount == 35 ||
                            !HookModel.get(owner).getHasHook() ||
                            owner.distanceTo(this) > maxRange ||
                            !(owner.getMainHandItem().getItem() instanceof HookshotItem ||
                                    owner.getOffhandItem().getItem() instanceof HookshotItem)) {

                        SPRITE = false;
                        kill();
                    }
                } else {
                    SPRITE = false;
                    kill();
                }

                if (owner.getMainHandItem() == stack || owner.getOffhandItem() == stack) {
                    if (isPulling) {
                        performPulling();
                    }
                } else {
                    SPRITE = false;
                    HookModel.get(owner).setHasHook(false);
                    kill();
                }
            }
        }
    }

    private void performPulling() {
        Entity target = owner;
        Entity origin = this;

        if (owner.isCrouching() && hookedEntity != null) {
            target = hookedEntity;
            origin = owner;
            owner.setNoGravity(true);
        }

        double pullSpeed = maxSpeed / 9D;
        Vec3 distance = origin.position().subtract(target.position().add(0, target.getBbHeight() / 2, 0));
        Vec3 motion = distance.normalize().scale(pullSpeed);

        // Adjust motion for ground level movement
        if (Math.abs(distance.y) < 0.1D) {
            motion = new Vec3(motion.x, 0, motion.z);
        } else if (new Vec3(distance.x, 0, distance.z).length() < target.getBbWidth() / 2 / 1.4) {
            motion = new Vec3(0, motion.y, 0);
        }

        target.fallDistance = 0;
        target.setDeltaMovement(motion);
        target.hurtMarked = true;

        if (hookedEntity != null) {
            if (distance.length() > prevDistance && prevDistance < 1) {
                kill();
                SPRITE = false;
                HookModel.get(owner).setHasHook(false);
            }
            if (tickCount > 50) {
                kill();
                SPRITE = false;
                HookModel.get(owner).setHasHook(false);
            }
        } else {
            if (distance.length() > prevDistance && prevDistance < 1) {
                kill();
                SPRITE = false;
                HookModel.get(owner).setHasHook(false);
            } else if (new Vec3(distance.x, 0, distance.z).length() < 0.3D) {
                kill();
                SPRITE = false;
                HookModel.get(owner).setHasHook(false);
            }
        }
        prevDistance = distance.length();

        // Handle item entity being hooked
        if (hookedEntity instanceof ItemEntity) {
            if (owner.getInventory().add(((ItemEntity) hookedEntity).getItem())) {
                SPRITE = false;
                HookModel.get(owner).setHasHook(false);
                kill();
            }
        }
    }



    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void kill() {
        if (!level().isClientSide && owner != null) {
            HookModel.get(owner).setHasHook(false);
            owner.setNoGravity(false);
            owner.setPose(Pose.STANDING);
            owner.setDeltaMovement(0, 0, 0);
        }
        owner.hurtMarked = true;
        super.kill();
    }

    @Override
    protected float getWaterInertia() {
        return 1.0F;
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);

        isPulling = true;
//        setHookableBlocks();

        if (!level().isClientSide() && owner != null && hookedEntity == null) {
            owner.setNoGravity(true);  // Disable gravity for smooth pulling

            // If using block list, check if the block is hookable
            if (useBlockList) {
                if (!hookableBlocks.contains(level().getBlockState(blockHitResult.getBlockPos()).getBlock())) {
                    HookModel.get(owner).setHasHook(false);
                    isPulling = false;
                    onRemovedFromWorld();
                    return;
                }
                List<ItemEntity> list = level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().expandTowards(1D, 0.5D, 1D));

                if(!list.isEmpty()){
                    for (Entity entity : list) {
                        hookedEntity = entity;
                    }
                    HookModel.get(owner).setHasHook(true);
                    isPulling = true;
                    onRemovedFromWorld();
                }
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (!level().isClientSide() && owner != null && entityHitResult.getEntity() != owner) {
            if ((entityHitResult.getEntity() instanceof LivingEntity || entityHitResult.getEntity() instanceof EnderDragonPart) && hookedEntity == null) {
                hookedEntity = entityHitResult.getEntity();
                entityData.set(HOOKED_ENTITY_ID, hookedEntity.getId() + 1);
                isPulling = true;
                owner.setNoGravity(true);
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        maxRange = tag.getDouble("maxRange");
        maxSpeed = tag.getDouble("maxSpeed");
        isPulling = tag.getBoolean("isPulling");
        stack = ItemStack.of(tag.getCompound("hookshotItem"));

        if (level().getEntity(tag.getInt("owner")) instanceof Player)
            owner = (Player) level().getEntity(tag.getInt("owner"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putDouble("maxRange", maxRange);
        tag.putDouble("maxSpeed", maxSpeed);
        tag.putBoolean("isPulling", isPulling);
        tag.put("hookshotItem", stack.save(new CompoundTag()));
        tag.putInt("owner", owner.getId());
    }

    public void setProperties(ItemStack stack, double maxRange, double maxVelocity, float pitch, float yaw, float roll, float modifierZ) {
        float f = 0.017453292F;
        float x = -Mth.sin(yaw * f) * Mth.cos(pitch * f);
        float y = -Mth.sin((pitch + roll) * f);
        float z = Mth.cos(yaw * f) * Mth.cos(pitch * f);
        this.shoot(x, y, z, modifierZ, 0);

        this.stack = stack;
        this.maxRange = maxRange;
        this.maxSpeed = maxVelocity;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    @Nonnull
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (true) {
            double maxRange = 15;
            List<HookshotEntity> entities = player.level().getEntitiesOfClass(HookshotEntity.class,
                    new AABB(player.blockPosition().offset((int) -maxRange, (int) -maxRange, (int) -maxRange), player.blockPosition().offset((int) maxRange, (int) maxRange, (int) maxRange))
            );
            for (HookshotEntity entity : entities) {
                if (entity.getOwner() == player) {
                    if (entity.isPulling) {
                        player.setPose(Pose.SWIMMING);
                        player.setSwimming(true);
                    }
                }
            }
        }
    }

    public static EntityType<HookshotEntity> createEntityType() {
        return EntityType.Builder.<HookshotEntity>of(HookshotEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build(SupersLegendMain.MOD_ID + ":hookshot");
    }
}