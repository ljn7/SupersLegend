package com.superworldsun.superslegend.items.customclass;

import com.superworldsun.superslegend.registries.SoundInit;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class BottledEntityItem extends Item {

	private final EntityType<? extends LivingEntity> entityType;

	public BottledEntityItem(EntityType<? extends LivingEntity> entityType) {
		super(new Item.Properties().stacksTo(1));
		this.entityType = entityType;
	}

	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		Level level = pContext.getLevel();
		if (!(level instanceof ServerLevel)) {
			return InteractionResult.SUCCESS;
		} else {
			ItemStack itemstack = pContext.getItemInHand();
			BlockPos blockpos = pContext.getClickedPos();
			Direction direction = pContext.getClickedFace();
			BlockState blockstate = level.getBlockState(blockpos);

			BlockPos blockpos1;
			if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
				blockpos1 = blockpos;
			} else {
				blockpos1 = blockpos.relative(direction);
			}
			Player player = pContext.getPlayer();
			if (entityType.spawn((ServerLevel)level, itemstack, pContext.getPlayer(), blockpos1, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP) != null) {
				level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundInit.BOTTLE_POP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
				player.getCooldowns().addCooldown(this, 6);
				level.gameEvent(pContext.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);
				player.setItemInHand(pContext.getHand(), new ItemStack(Items.GLASS_BOTTLE));
			}

			return InteractionResult.CONSUME;
		}
	}


	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide) {
			LivingEntity entity = entityType.create(level);
			if (entity != null) {
				Vec3 lookVec = player.getLookAngle();
				double spawnDistance = 2.0;
				double x = player.getX() + lookVec.x * spawnDistance;
				double y = player.getY();
				double z = player.getZ() + lookVec.z * spawnDistance;

				entity.moveTo(x, y, z, player.getYRot(), 0f);
				level.addFreshEntity(entity);
			}
		}
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundInit.BOTTLE_POP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
		player.getCooldowns().addCooldown(this, 6);
		return InteractionResultHolder.sidedSuccess(new ItemStack(Items.GLASS_BOTTLE), level.isClientSide());
	}


	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
		{
			tooltip.add(Component.literal("A captive Bug in a bottle").withStyle(ChatFormatting.GREEN));
			tooltip.add(Component.literal("Right-Click to Release").withStyle(ChatFormatting.DARK_GRAY));
		}
		super.appendHoverText(stack, level, tooltip, flag);
	}
}
