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
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;

public class SeaBreezeBoomerangEntity extends AbstractBoomerangEntity {
    public SeaBreezeBoomerangEntity(EntityType<SeaBreezeBoomerangEntity> type, Level level) {
        super(type, level);
        turnBackTimer = 60;
        getDefaultSpeed = 1f;
        turningBack = true;
    }

    public SeaBreezeBoomerangEntity(Player owner, ItemStack stack) {
        super(EntityTypeInit.SEA_BREEZE_BOOMERANG.get(), owner, stack);
    }

    @Override
    protected SoundEvent getFlyLoopSound() {
        return SoundInit.WW_BOOMERANG_FLY_LOOP.get();
    }


    public void beforeTurnAround(Player player) {
        // Follows where the entity is looking
        {
            double x = -MathHelper.wrapDegrees((player.yRotO * 3.141593F) / 180F);
            double z = MathHelper.wrapDegrees((player.yRotO * 3.141593F) / 180F);

            double motionX = 0.5D * x * (double) MathHelper.wrapDegrees((player.yRotO / 180F) * 3.141593F);
            double motionY = -0.5D * (double) MathHelper.wrapDegrees((player.yRotO / 180F) * 3.141593F);
            double motionZ = 0.5D * z * (double) MathHelper.wrapDegrees((player.yRotO / 180F) * 3.141593F);
            this.setDeltaMovement(motionX, motionY, motionZ);
        }
    }

    @Override
    protected float getDamage() {
        return 1f;
    }

    public static EntityType<SeaBreezeBoomerangEntity> createEntityType() {
        return EntityType.Builder.<SeaBreezeBoomerangEntity>of(SeaBreezeBoomerangEntity::new, MobCategory.MISC)
                .sized(0.5f, 0.5f)
                .build(SupersLegendMain.MOD_ID + ":sea_breeze_boomerang");
    }
}
