package com.superworldsun.superslegend.entities.projectiles.boomerang;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.registries.EntityTypeInit;
import com.superworldsun.superslegend.registries.SoundInit;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MagicBoomerangEntity extends AbstractBoomerangEntity {
    public MagicBoomerangEntity(EntityType<MagicBoomerangEntity> type, Level level) {
        super(type, level);
        turnBackTimer = 60;
        getDefaultSpeed = 1f;
    }

    public MagicBoomerangEntity(Player owner, ItemStack stack) {
        super(EntityTypeInit.MAGIC_BOOMERANG.get(), owner, stack);
    }

    @Override
    protected SoundEvent getFlyLoopSound() {
        return SoundInit.ALTTP_BOOMERANG_FLY_LOOP.get();
    }

    @Override
    protected float getDamage() {
        return 1f;
    }

    public static EntityType<MagicBoomerangEntity> createEntityType() {
        return EntityType.Builder.<MagicBoomerangEntity>of(MagicBoomerangEntity::new, MobCategory.MISC)
                .sized(0.5f, 0.5f)
                .build(SupersLegendMain.MOD_ID + ":magic_boomerang");
    }
}
