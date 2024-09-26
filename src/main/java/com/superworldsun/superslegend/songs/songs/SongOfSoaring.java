package com.superworldsun.superslegend.songs.songs;

import com.superworldsun.superslegend.capability.waypoint.Waypoint;
import com.superworldsun.superslegend.capability.waypoint.Waypoints;
import com.superworldsun.superslegend.capability.waypoint.WaypointsProvider;
import com.superworldsun.superslegend.network.NetworkDispatcher;
import com.superworldsun.superslegend.network.message.ShowWaystonesScreenMessage;
import com.superworldsun.superslegend.registries.SoundInit;
import com.superworldsun.superslegend.songs.OcarinaSong;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import java.util.Iterator;
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
		boolean noWaypoints = WaypointsProvider.get(Minecraft.getInstance().level.getPlayerByUUID(player.getUUID())).getWaypoints().isEmpty();

		if (noWaypoints) {
			player.sendSystemMessage(Component.translatable("song.superslegend.song_of_soaring.no_statues")
					.withStyle(ChatFormatting.DARK_RED));
		} else {
			NetworkDispatcher.network_channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new ShowWaystonesScreenMessage(player.getUUID()));
		}
	}

	private static void removeNonExistentWaypoints(ServerPlayer player) {
		Waypoints playerWaypointsData = WaypointsProvider.get(player);
		Iterator<Waypoint> waypointsIterator = playerWaypointsData.getWaypoints().iterator();

		boolean syncRequired = false;

		while (waypointsIterator.hasNext()) {
			Waypoint waypoint = waypointsIterator.next();
			boolean doesWaypointExist = playerWaypointsData.getWaypoint(waypoint.getStatuePosition()) != null;
			if (!doesWaypointExist) {
				playerWaypointsData.removeWaypoint(waypoint.getStatuePosition());
				syncRequired = true;
			}
		}

		if (syncRequired) {
			WaypointsProvider.sync(player);
		}
	}
}
