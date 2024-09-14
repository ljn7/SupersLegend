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
	private static final long TICKS_PER_DAY = 24000L;
	private static long lastTickTime = 0;
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
		if (event.phase != TickEvent.Phase.END) return;

		ServerLevel serverLevel = event.getServer().getLevel(Level.OVERWORLD);
		if (serverLevel == null) return;

		int tickSpeed = serverLevel.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);

		if (tickSpeed == 6) {
			long currentTime = serverLevel.getDayTime();
			long elapsedTime = currentTime - lastTickTime;

			long newTime = (currentTime + elapsedTime) % TICKS_PER_DAY;
			serverLevel.setDayTime(newTime);

			lastTickTime = newTime;

			timer--;
			if (timer <= 0) {
				serverLevel.getGameRules().getRule(GameRules.RULE_RANDOMTICKING).set(3, event.getServer());
			}
		} else {
			lastTickTime = serverLevel.getDayTime();
		}
	}
}
