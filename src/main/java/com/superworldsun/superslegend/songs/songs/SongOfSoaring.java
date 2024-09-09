package com.superworldsun.superslegend.songs.songs;

import com.superworldsun.superslegend.network.NetworkDispatcher;
import com.superworldsun.superslegend.network.message.ShowWaystonesScreenMessage;
import com.superworldsun.superslegend.registries.SoundInit;
import com.superworldsun.superslegend.songs.OcarinaSong;
import com.superworldsun.superslegend.waypoints.WaypointsManager;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class SongOfSoaring extends OcarinaSong {
	public SongOfSoaring() {
		super("dludlu", 0xCDD5E3);
	}

	@Override
	public SoundEvent getPlayingSound() {
		return SoundInit.SONG_OF_SOARING.get();
	}

	@Override
	public void onSongPlayed(Player player, Level level) {
		if (!(player instanceof ServerPlayer serverPlayer)) {
			return;
		}

		removeNonExistentWaypoints(serverPlayer);
		List<WaypointsManager.Waypoint> waypoints = WaypointsManager.getWaypoints(player);

		if (waypoints.isEmpty()) {
			player.sendSystemMessage(Component.translatable("song.superslegend.song_of_soaring.no_statues"));
		} else {
			NetworkDispatcher.network_channel.send(
					PacketDistributor.PLAYER.with(() -> serverPlayer),
					new ShowWaystonesScreenMessage()
			);
		}
	}

	private static void removeNonExistentWaypoints(ServerPlayer player) {
		List<WaypointsManager.Waypoint> waypoints = WaypointsManager.getWaypoints(player);
		boolean syncRequired = false;

		for (WaypointsManager.Waypoint waypoint : waypoints) {
			if (WaypointsManager.getWaypoint(player, waypoint.getStatuePosition()) == null) {
				WaypointsManager.removeWaypoint(player, waypoint.getStatuePosition());
				syncRequired = true;
			}
		}

		if (syncRequired) {
			// You might need to implement a method to sync waypoints to the client
			WaypointsManager.syncToClient(player);
		}
	}
}
