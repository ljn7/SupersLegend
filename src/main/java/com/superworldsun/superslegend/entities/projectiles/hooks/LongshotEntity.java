package com.superworldsun.superslegend.entities.projectiles.hooks;


import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.capability.hookshot.HookModel;
import com.superworldsun.superslegend.items.hookshot.HookshotItem;
import com.superworldsun.superslegend.items.hookshot.LongshotItem;
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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.List;

import static com.superworldsun.superslegend.items.hookshot.LongshotItem.LONG_SPRITE;
import static com.superworldsun.superslegend.util.HookBlockList.hookableBlocks;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LongshotEntity extends AbstractArrow {

    /**
     * useBlockList serves to enable or disable the block list that can be hooked on the hook.
     * isPulling activates the player's movement. Do not touch this value.
     * motionUp lets you know when the player goes up to get off the hook. Do not touch this value.
     */
    private static final EntityDataAccessor<Integer> HOOKED_ENTITY_ID = SynchedEntityData.defineId(LongshotEntity.class, EntityDataSerializers.INT);
    boolean useBlockList = true;
    private double maxRange = 0D;
    private double maxSpeed = 0D;
    private boolean isPulling = false;
    private Player owner;
    private Entity hookedEntity;
    private ItemStack stack;
    private boolean motionUp = false;
    private double prevDistance = 30D;


    public LongshotEntity(EntityType<? extends AbstractArrow> type, LivingEntity owner, Level world) {
        super(type, owner, world);
        this.setSoundEvent(SoundInit.HOOKSHOT_TARGET.get());
        this.setNoGravity(true);
        this.setBaseDamage(0);

    }

    public LongshotEntity(EntityType<LongshotEntity> longshotEntityEntityType, Level world) {
        super(EntityTypeInit.LONGSHOT_ENTITY.get(), world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HOOKED_ENTITY_ID, 0);
    }

    /**
     * This is where everything related to movement happens.
     */
    @Override
    public void tick() {
        super.tick();

        if(this.tickCount % 3 == 0)
        {
            BlockPos currentPos = this.blockPosition();
            this.level().playSound(null, currentPos.getX(), currentPos.getY(), currentPos.getZ(), SoundInit.HOOKSHOT_EXTENDED.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        }

        if (getOwner() instanceof Player) {
            owner = (Player) getOwner();

            if (isPulling && tickCount % 2 == 0) { //This is the sound that sounds when the hook is moving you.
                //level.playSound(null, owner.blockPosition(), SoundEvents.AXE_STRIP, SoundCategory.PLAYERS, 1F, 1F);
            }
            if (!level().isClientSide) {
                if (this.hookedEntity != null) { //In case the mob you are hooked to dies while you go towards it ..
                    if (!this.hookedEntity.isAlive()) {
                        this.hookedEntity = null;
                        onRemovedFromWorld();
                    } else {
                        this.absMoveTo(this.hookedEntity.getX(), this.hookedEntity.getY(0.8D), this.hookedEntity.getZ());
                    }
                }

                if (owner != null) { //Reasons to remove the hook.
                    if (owner.isDeadOrDying() || this.tickCount == 65 || !HookModel.get(owner).getHasHook() ||
                            !HookModel.get(owner).getHasHook() ||
                            owner.distanceTo(this) > maxRange ||
                            !(owner.getMainHandItem().getItem() instanceof LongshotItem ||
                                    owner.getOffhandItem().getItem() instanceof LongshotItem) ||
                            !HookModel.get(owner).getHasHook()) {

                        LONG_SPRITE = false;
                        kill();

                    }
                } else {
                    LONG_SPRITE = false;
                    kill();
                }

                if (owner.getMainHandItem() == stack || owner.getOffhandItem() == stack) {
                    if (isPulling) { //Movement start
                        Entity target = owner;
                        Entity origin = this;

                        if (owner.isCrouching() && hookedEntity != null) {
                            target = hookedEntity;
                            origin = owner;
                            owner.setNoGravity(true);
                        }

                        double brakeZone = ((6D * (maxSpeed)) / 10); //5
                        double pullSpeed = (maxSpeed) / 9D;
                        Vec3 distance = origin.position().subtract(target.position().add(0, target.getBbHeight() / 2, 0));
                        double reduction = (pullSpeed); //Get motion reduction.
                        Vec3 motion = distance.normalize().multiply(reduction, reduction, reduction); //Get last motion.

                        //In case the movement is at ground level.
                        if (Math.abs(distance.y) < 0.1D) {
                            motion = new Vec3(motion.x, 0, motion.z);
                        }
                        //In case the movement is only upwards.
                        else if (new Vec3(distance.x, 0, distance.z).length() < new Vec3(target.getBbWidth() / 2, 0, target.getBbWidth() / 2).length() / 1.4) {
                            motion = new Vec3(0, motion.y, 0);
                            motionUp = true;
                        }

                        target.fallDistance = 0; //Cancel Fall Damage

                        target.setDeltaMovement(motion); //Set motion.
                        target.hurtMarked = true; //Make motion works, this is necessary.

                        //Makes you off the hook early if entity.
                        if(hookedEntity != null){
                            motion = owner.getDeltaMovement();
                            if (distance.length() > prevDistance && prevDistance < 1){
                                kill();
                                LONG_SPRITE = false;
                                HookModel.get(owner).setHasHook(false);
                            }
                            //Timer if the entity if too BIG.
                            if(tickCount > 50){
                                kill();
                                LONG_SPRITE = false;
                                HookModel.get(owner).setHasHook(false);
                            }
                        }
                        //Makes you off the hook early if block.
                        if(hookedEntity == null) {
                            motion = owner.getDeltaMovement();
                            if (distance.length() > prevDistance && prevDistance < 1){
                                kill();
                                LONG_SPRITE = false;
                                HookModel.get(owner).setHasHook(false);
                            } else if (new Vec3(distance.x, 0, distance.z).length() < 0.3D) {
                                kill();
                                LONG_SPRITE = false;
                                HookModel.get(owner).setHasHook(false);

                            }
                        }
                        prevDistance = distance.length();

                        //Take the entity if it is an item and check that it is in your inventory to kill the hook.
                        if(hookedEntity instanceof ItemEntity){
                            if(owner.getInventory().add(((ItemEntity) hookedEntity).getItem())) {
                                LONG_SPRITE = false;
                                HookModel.get(owner).setHasHook(false);
                                kill();

                            }
                        }

                    }

                } else {
                    LONG_SPRITE = false;
                    HookModel.get(owner).setHasHook(false);
                    kill();

                }
            }
        }
    }

    //Prevents a crash. Name self-explanatory.
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

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
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (this.getOwner() instanceof Player player) {
            LongshotItem.resetSprite(player);
        }
    }

    /**
     * This function is used to make the hook go slower or faster in water.
     * Currently it has no value.
     */
    @Override
    protected float getWaterInertia() {
        return 1.0F;
    }

    /**
     * This function is used to detect when the hook hits an object.
     * It is also used to collect items from the ground.
     * @param blockHitResult
     */
    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        isPulling = true;

        if (!level().isClientSide && owner != null && hookedEntity == null) {
            owner.setNoGravity(true);

            //Initialization of the list of ItemEntities found on the floor
            // and selection of the size of the Bounding Box in which to search for them.
            List<ItemEntity> list = level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().expandTowards(1D, 0.5D, 1D));

            if (useBlockList) { //If this value is "FALSE" all blocks will be hooked.
                //If the block is not in the list, the hook does not hook.
                if (!hookableBlocks.contains(level().getBlockState(blockHitResult.getBlockPos()).getBlock())) {
                    HookModel.get(owner).setHasHook(false);
                    isPulling = false;
                    onRemovedFromWorld();
                }
                //Catch Items
                if(list != null && list.size() > 0){
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

    /**
     * This function is used to detect when the hook hits an entity.
     * @param entityHitResult
     */
    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (!level().isClientSide && owner != null && entityHitResult.getEntity() != owner) {
            if((entityHitResult.getEntity() instanceof LivingEntity || entityHitResult.getEntity() instanceof EnderDragonPart) && hookedEntity == null) {
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

    /**
     * Used to get the properties from the item.
     */
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

    //Disable ChangeDimensions.
    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    //Make the entity appear in the level.
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
            List<LongshotEntity> entities = player.level().getEntitiesOfClass(LongshotEntity.class,
                    new AABB(player.blockPosition().offset((int)-maxRange, (int)-maxRange, (int)-maxRange), player.blockPosition().offset((int)maxRange, (int)maxRange, (int)maxRange)));
            for(LongshotEntity entity : entities) {
                if(entity.getOwner() == player) {
                    if (entity.isPulling) {
                        player.setPose(Pose.SWIMMING);
                        player.setSwimming(true);
                    }
                }
            }
        }
    }

    public static EntityType<LongshotEntity> createEntityType()
    {
        return EntityType.Builder.<LongshotEntity>of(LongshotEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build(SupersLegendMain.MOD_ID + ":longshot");
    }
}
