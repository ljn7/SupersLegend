package com.superworldsun.superslegend.entities.projectiles.seeds;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.registries.EntityTypeInit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CocoaBeanEntity extends SeedEntity {
    private static final float HITBOX_HEIGHT = 0.5f;
    private static final float HITBOX_WIDTH = 0.5f;

    public CocoaBeanEntity(EntityType<? extends CocoaBeanEntity> type, Level level) {
        super(type, level);
    }

    public CocoaBeanEntity(Level level) {
        super(EntityTypeInit.COCOA_BEAN.get(), level);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(Items.COCOA_BEANS);
    }

    @Override
    protected float getMass() {
        return 0.15f;
    }

    public static EntityType<CocoaBeanEntity> createEntityType() {
        return EntityType.Builder.<CocoaBeanEntity>of(CocoaBeanEntity::new, MobCategory.MISC)
                .sized(HITBOX_WIDTH, HITBOX_HEIGHT)
                .build(SupersLegendMain.MOD_ID + ":cocoa_bean");
    }
}