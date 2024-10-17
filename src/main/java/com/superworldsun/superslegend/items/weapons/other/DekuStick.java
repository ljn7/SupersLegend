package com.superworldsun.superslegend.items.weapons.other;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.superworldsun.superslegend.items.customclass.NonEnchantItem;
import com.superworldsun.superslegend.registries.BlockInit;
import com.superworldsun.superslegend.registries.ItemInit;
import com.superworldsun.superslegend.registries.SoundInit;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class DekuStick extends NonEnchantItem {
	public DekuStick() {
		super(new Properties().stacksTo(16));
	}

	// TODO: Implement custom break sound
	private static final SoundEvent BREAK_SOUND = SoundEvents.ITEM_BREAK; // Placeholder

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(Component.literal("A stick").withStyle(ChatFormatting.YELLOW));
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
		if (!player.level().isClientSide()) {
			entity.hurt(player.level().damageSources().generic(), 8.0F);
			if (!player.getAbilities().instabuild) {
				playBreakEffects(player);
				stack.shrink(1);
			}
		}
		return true;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();
		ItemStack stack = context.getItemInHand();

		if (level.isClientSide() || player == null) return InteractionResult.SUCCESS;

		Block targetBlock = level.getBlockState(pos).getBlock();
		if (targetBlock == Blocks.FIRE || targetBlock == BlockInit.TORCH_TOWER_TOP_LIT.get()) {
			playIgniteSound(level, player.blockPosition());
			stack.shrink(1);
			player.addItem(new ItemStack(ItemInit.DEKU_STICK_LIT.get()));
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	private void playBreakEffects(Player player) {
		player.playSound(BREAK_SOUND, 1F, 1F);
		spawnBreakParticles(player);
	}

	private void playIgniteSound(Level level, BlockPos pos) {
		level.playSound(null, pos, SoundInit.FIRE_IGNITE.get(), SoundSource.PLAYERS, 1f, 1f);
	}

	private void spawnBreakParticles(Player player) {
		RandomSource rand = player.level().getRandom();
		for (int i = 0; i < 10; i++) {
			player.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(ItemInit.DEKU_STICK.get())),
					player.getX() + (rand.nextBoolean() ? -1 : 1) * Math.pow(rand.nextFloat(), 1) * 0.2f,
					player.getY() + rand.nextFloat() * 1 - -1,
					player.getZ() + (rand.nextBoolean() ? -1 : 1) * Math.pow(rand.nextFloat(), 1) * 0.2f,
					0, 0.105D, 0);
		}
	}
}