package com.superworldsun.superslegend.songs.songs;

import static net.minecraft.world.level.GameRules.RULE_RANDOMTICKING;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.registries.SoundInit;
import com.superworldsun.superslegend.songs.OcarinaSong;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
	public void onSongPlayed(Player player, Level level) {
		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}

		GameRules gameRules = serverLevel.getGameRules();
		int currentTickingSpeed = gameRules.getInt(GameRules.RULE_RANDOMTICKING);
		if (currentTickingSpeed != 6) {
			player.sendSystemMessage(Component.translatable("text.ocarina.doubled", player.getName()).withStyle(ChatFormatting.GREEN));
			gameRules.getRule(GameRules.RULE_RANDOMTICKING).set(6, serverLevel.getServer());
			timer = 24000 * 3;
		} else {
			for (ServerPlayer p : serverLevel.getServer().getPlayerList().getPlayers()) {
				p.sendSystemMessage(Component.translatable("text.ocarina.doubled_second", p.getName()).withStyle(ChatFormatting.YELLOW));
			}
			gameRules.getRule(GameRules.RULE_RANDOMTICKING).set(3, serverLevel.getServer());
			timer = 0;
		}
	}

	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event) {
		timer--;
		if (timer <= 0) {
			ServerLevel overworld = event.getServer().overworld();
			if (overworld.getGameRules().getInt(GameRules.RULE_RANDOMTICKING) == 6) {
				overworld.getGameRules().getRule(GameRules.RULE_RANDOMTICKING).set(3, event.getServer());
			}
		}
	}

	@SubscribeEvent
	public static void onWorldTick(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			ServerLevel level = event.getServer().getLevel(Level.OVERWORLD);
			if (level == null) return;

			int tickSpeed = level.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
			if (tickSpeed == 6) {
				level.setDayTime(level.getDayTime() + 3);
			}
		}
	}
}
