package com.superworldsun.superslegend.songs.songs;

import java.util.List;

import com.superworldsun.superslegend.registries.SoundInit;
import com.superworldsun.superslegend.songs.OcarinaSong;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class SonataOfAwakening extends OcarinaSong {
	private static final int EFFECT_RADIUS = 5;

	public SonataOfAwakening() {
		super("ululara", 0x00A915);
	}

	@Override
	public SoundEvent getPlayingSound() {
		return SoundInit.SONATA_OF_AWAKENING.get();
	}

	@Override
	public void onSongPlayed(Player player, Level world) {
		getPlayersInAreaOfEffect(player, world).stream().filter(Player::isSleeping).forEach(Player::stopSleeping);
	}

	private List<Player> getPlayersInAreaOfEffect(Player player, Level world) {
		AABB areaOfEffect = player.getBoundingBox().inflate(EFFECT_RADIUS);
		return world.getEntitiesOfClass(Player.class, areaOfEffect,
				nearbyPlayer -> nearbyPlayer != player && nearbyPlayer.distanceToSqr(player) <= EFFECT_RADIUS * EFFECT_RADIUS);
	}
}
