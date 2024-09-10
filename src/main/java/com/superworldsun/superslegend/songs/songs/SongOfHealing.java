package com.superworldsun.superslegend.songs.songs;

import java.util.List;
import java.util.Map;

import com.superworldsun.superslegend.registries.SoundInit;
import com.superworldsun.superslegend.songs.OcarinaSong;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;


public class SongOfHealing extends OcarinaSong
{
    private static double radius = 10D;
    public SongOfHealing()
    {
        super("lrdlrd", 0xD560D8);
    }

    @Override
    public SoundEvent getPlayingSound()
    {
        return SoundInit.SONG_OF_HEALING.get();
    }

    @Override
    public void onSongPlayed(Player player, Level level) {

        List<Entity> entities = level.getEntities((Entity) null, player.getBoundingBox().inflate(radius), entity -> player.distanceTo(entity) < radius);
        entities.forEach(entity ->
        {
            if (entity instanceof LivingEntity livingEntity)
            {
                livingEntity.removeEffect(MobEffects.POISON);
                livingEntity.removeEffect(MobEffects.WITHER);
                livingEntity.removeEffect(MobEffects.WEAKNESS);
                livingEntity.removeEffect(MobEffects.BLINDNESS);

                entity.getArmorSlots().forEach(stack ->
                {
                    Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
                    enchantments.entrySet().removeIf(entry -> entry.getKey().isCurse());
                    EnchantmentHelper.setEnchantments(enchantments, stack);
                });
            }
        });
    }

}