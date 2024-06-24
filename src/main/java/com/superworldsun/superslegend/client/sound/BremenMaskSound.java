package com.superworldsun.superslegend.client.sound;

import com.superworldsun.superslegend.interfaces.IMaskAbility;
import com.superworldsun.superslegend.registries.ItemInit;
import com.superworldsun.superslegend.registries.SoundInit;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

public class BremenMaskSound extends AbstractTickableSoundInstance {
	private final Player player;

	public BremenMaskSound(Player player) {
		super(SoundInit.BREMEN_MARCH.get(), SoundSource.PLAYERS , RandomSource.create());
		this.player = player;
		this.looping = true;
		this.delay = 0;
		this.volume = 1.0F;
		this.x = player.getX();
		this.y = player.getY();
		this.z = player.getZ();
	}

	@Override
	public boolean canPlaySound() {
		return !player.isSilent();
	}

	@Override
	public boolean canStartSilent() {
		return true;
	}

	@Override
	public void tick() {
		if (!player.isAlive()) {
			stop();
			return;
		}

		ItemStack maskStack = CuriosApi.getCuriosHelper().findEquippedCurio(ItemInit.MASK_BREMANMASK.get(), player)
				.map(ImmutableTriple::getRight).orElse(ItemStack.EMPTY);

		if (!maskStack.isEmpty()) {
			if (maskStack.getItem() != ItemInit.MASK_BREMANMASK.get()
					|| !((IMaskAbility) maskStack.getItem()).isPlayerUsingAbility(player)) {
				stop();
				return;
			}
		}

		x = player.getX();
		y = player.getY();
		z = player.getZ();
	}
}
