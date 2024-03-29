package com.superworldsun.superslegend.entities.projectiles.seeds;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.registries.EntityTypeInit;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class WheatSeedEntity extends SeedEntity {
    private static final float HITBOX_HEIGHT = 0.5f;
    private static final float HITBOX_WIDTH = 0.5f;
    private static final float WATER_INERTIA = 0.3f;
    private static final float BASE_DAMAGE = 0.5f;

    public WheatSeedEntity(EntityType<? extends WheatSeedEntity> type, Level level) {
        super(type, level);
    }

    public WheatSeedEntity(Level level) {
        super(EntityTypeInit.WHEAT_SEED.get(), level);
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.CROP_BREAK;
    }

    protected float getWaterInertia() {
        return WATER_INERTIA;
    }

    @Override
    protected float getDamage() {
        return BASE_DAMAGE;
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(Items.WHEAT_SEEDS);
    }

    public static EntityType<WheatSeedEntity> createEntityType() {
        return EntityType.Builder.<WheatSeedEntity>of(WheatSeedEntity::new, MobCategory.MISC)
                .sized(HITBOX_WIDTH, HITBOX_HEIGHT)
                .build(SupersLegendMain.MOD_ID + ":wheat_seed");
    }
}