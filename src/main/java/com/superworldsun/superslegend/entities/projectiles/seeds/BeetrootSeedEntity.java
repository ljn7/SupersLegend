package com.superworldsun.superslegend.entities.projectiles.seeds;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.registries.EntityTypeInit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BeetrootSeedEntity extends SeedEntity {
    private static final float HITBOX_HEIGHT = 0.5f;
    private static final float HITBOX_WIDTH = 0.5f;

    public BeetrootSeedEntity(EntityType<? extends BeetrootSeedEntity> type, Level level) {
        super(type, level);
    }

    public BeetrootSeedEntity(Level level) {
        super(EntityTypeInit.BEETROOT_SEED.get(), level);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(Items.BEETROOT_SEEDS);
    }

    public static EntityType<BeetrootSeedEntity> createEntityType() {
        return EntityType.Builder.<BeetrootSeedEntity>of(BeetrootSeedEntity::new, MobCategory.MISC)
                .sized(HITBOX_WIDTH, HITBOX_HEIGHT)
                .build(SupersLegendMain.MOD_ID + ":beetroot_seed");
    }
}