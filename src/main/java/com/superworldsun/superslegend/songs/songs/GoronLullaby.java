package com.superworldsun.superslegend.songs.songs;

import com.superworldsun.superslegend.registries.SoundInit;
import com.superworldsun.superslegend.songs.OcarinaSong;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;


public class GoronLullaby extends OcarinaSong {
    private final static int RADIUS = 5;
	public GoronLullaby()
	{
		super("arlarlra", 0x840101);
	}

	@Override
	public SoundEvent getPlayingSound()
	{
		return SoundInit.GORON_LULLABY.get();
	}

    @Override
    public void onSongPlayed(Player player, Level level) {
        AABB searchArea = player.getBoundingBox().inflate(RADIUS);

        level.getEntitiesOfClass(Mob.class, searchArea).forEach(entity -> {
            if (entity instanceof NeutralMob neutralMob) {
                neutralMob.playerDied(player);
            }
        });
    }
}
