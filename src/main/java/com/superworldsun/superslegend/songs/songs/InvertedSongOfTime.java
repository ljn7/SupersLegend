package com.superworldsun.superslegend.songs.songs;

import static net.minecraft.world.level.GameRules.RULE_RANDOMTICKING;

import com.superworldsun.superslegend.registries.SoundInit;
import com.superworldsun.superslegend.songs.OcarinaSong;


import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;


public class InvertedSongOfTime extends OcarinaSong
{
	public InvertedSongOfTime()
	{
		super("dardar", 0x3170CF);
	}

	// TODO: this is not being saved when the game is closed
	static int timer;

	@Override
	public SoundEvent getPlayingSound()
	{
		return SoundInit.INVERTED_SONG_OF_TIME.get();
	}

	@Override
	public void onSongPlayed(Player player, Level level)
	{
		ServerLevel serverWorld = (ServerLevel) level;
		MinecraftServer minecraftServer = serverWorld.getServer();

		if (serverWorld.getServer().getGameRules().getRule(RULE_RANDOMTICKING).get() != 1)
		{
			player.sendSystemMessage(Component.translatable("text.ocarina.inverted", player.getName()));
			setGameRule(minecraftServer, RULE_RANDOMTICKING, 1);
			timer = 24000 * 3;
		}
		else
		{
			player.sendSystemMessage(Component.translatable("text.ocarina.inverted_second", player.getName()));
			setGameRule(minecraftServer, RULE_RANDOMTICKING, 3);
			timer = 0;

		}
	}

	@SubscribeEvent
	public static void onWorldTick(TickEvent.ServerTickEvent event)
	{
		MinecraftServer minecraftServer = ServerLifecycleHooks.getCurrentServer();
		timer = timer - 1;

		if (timer <= 0 && minecraftServer.getGameRules().getRule(RULE_RANDOMTICKING).get() == 1)
		{
			setGameRule(minecraftServer, RULE_RANDOMTICKING, 3);
		}
	}

    public static void setGameRule(MinecraftServer minecraftServer, GameRules.Key<GameRules.IntegerValue> ruleKey, int value) {
        GameRules gameRules = minecraftServer.getGameRules();
        GameRules.IntegerValue rule = gameRules.getRule(ruleKey);
        rule.set(value, minecraftServer);
    }
}
