package com.superworldsun.superslegend.entities.projectiles.seeds;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.registries.EntityTypeInit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PumpkinSeedEntity extends SeedEntity {
    private static final float HITBOX_HEIGHT = 0.5f;
    private static final float HITBOX_WIDTH = 0.5f;

    public PumpkinSeedEntity(EntityType<? extends PumpkinSeedEntity> type, Level level) {
        super(type, level);
    }

    public PumpkinSeedEntity(Level level) {
        super(EntityTypeInit.PUMPKIN_SEED.get(), level);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(Items.PUMPKIN_SEEDS);
    }

    public static EntityType<PumpkinSeedEntity> createEntityType() {
        return EntityType.Builder.<PumpkinSeedEntity>of(PumpkinSeedEntity::new, MobCategory.MISC)
                .sized(HITBOX_WIDTH, HITBOX_HEIGHT)
                .build(SupersLegendMain.MOD_ID + ":pumpkin_seed");
    }
}