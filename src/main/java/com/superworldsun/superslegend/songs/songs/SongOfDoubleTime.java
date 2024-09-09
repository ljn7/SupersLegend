package com.superworldsun.superslegend.songs.songs;

import static net.minecraft.world.level.GameRules.RULE_RANDOMTICKING;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.registries.SoundInit;
import com.superworldsun.superslegend.songs.OcarinaSong;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = SupersLegendMain.MOD_ID)
public class SongOfDoubleTime extends OcarinaSong
{
	public SongOfDoubleTime()
	{
		super("rraadd", 0x023A91);
	}
	
	// TODO: this is not being saved when the game is closed
	static int timer;
	
	@Override
	public SoundEvent getPlayingSound()
	{
		return SoundInit.SONG_OF_DOUBLE_TIME.get();
	}
	
	@Override
	public void onSongPlayed(Player player, Level level)
	{
		ServerLevel serverWorld = (ServerLevel) level;
		MinecraftServer minecraftServer = serverWorld.getServer();
		
		if (minecraftServer.getGameRules().getRule(RULE_RANDOMTICKING).get() != 6)
		{
			player.sendSystemMessage(Component.translatable("text.ocarina.doubled", player.getName()));
			setGameRule(minecraftServer, 6);
			timer = 24000 * 3;
		}
		else
		{
			minecraftServer.getPlayerList().getPlayers().forEach(p -> p.sendSystemMessage(
					Component.translatable("text.ocarina.doubled_second", p.getName())));
			setGameRule(minecraftServer, 3);
			timer = 0;
		}
	}
	
	@SubscribeEvent
	public static void onWorldTick(TickEvent.ServerTickEvent event)
	{
		MinecraftServer minecraftServer = ServerLifecycleHooks.getCurrentServer();
		timer = timer - 1;
		
		if (timer <= 0 && minecraftServer.getGameRules().getRule(RULE_RANDOMTICKING).get() == 6)
		{
			setGameRule(minecraftServer, 3);
		}
	}

	private static void setGameRule(MinecraftServer minecraftServer, int value) {
		GameRules gameRules = minecraftServer.getGameRules();
		GameRules.IntegerValue gameRule = gameRules.getRule(GameRules.RULE_RANDOMTICKING);

		gameRule.set(value, minecraftServer);

		minecraftServer.getPlayerList().getPlayers().forEach(player -> {
			player.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.CHANGE_GAME_MODE, 0));
			player.connection.send(new ClientboundEntityEventPacket(player, (byte)70));
		});
	}
}
