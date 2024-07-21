package com.superworldsun.superslegend.blocks.entity;

import com.superworldsun.superslegend.container.PostboxContainer;
import com.superworldsun.superslegend.inventory.PostboxInventory;
import com.superworldsun.superslegend.menus.PostboxMenu;
import com.superworldsun.superslegend.registries.BlockEntityInit;
import com.superworldsun.superslegend.registries.BlockInit;
import com.superworldsun.superslegend.registries.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import net.minecraftforge.items.ItemStackHandler;

public class PostboxBlockEntity extends BlockEntity implements MenuProvider {
	private final PostboxInventory inventory = new PostboxInventory(this);
	private boolean isLocked;
	private final LazyOptional<PostboxInventory> optional = LazyOptional.of(() -> this.inventory);

	public PostboxBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.POSTBOX_ENTITY.get(), pos, state);
	}

	public void dropInventoryContents() {
		Containers.dropContents(level, getBlockPos(), inventory);
	}

	public void interact(ServerPlayer player, InteractionHand hand, BlockState state, Level level, BlockPos pos) {
		if (player.isCrouching()) {
			if (isPlayerPostman(player)) {
				toggleLockedState(player);
			}
		} else {
			if (canBeOpenedByPlayer(player)) {
				NetworkHooks.openScreen(player, new SimpleMenuProvider(
						(containerId, playerInventory, pPlayer) -> new PostboxMenu(containerId, playerInventory),
						Component.translatable("Postbox")
				));
				level.playSound(null, player, SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS, 1F, 1F);
			} else {
				ItemStack itemInHand = player.getItemInHand(hand);

				if (!addItemIntoInventory(itemInHand, 1)) {
					level.playSound(null, player, SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1F, 1F);
				}
			}
		}
	}

	private boolean addItemIntoInventory(ItemStack itemInHand, int amount) {
		if (itemInHand.getCount() < amount) {
			return false;
		}
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			if (addItemInSlot(itemInHand, i, amount)) {
				return true;
			}
		}
		return false;
	}

	protected boolean addItemInSlot(ItemStack itemStack, int slotIndex, int amount) {
		ItemStack itemInSlot = inventory.getItem(slotIndex);
		boolean isSlotFull = itemInSlot.getCount() == itemInSlot.getMaxStackSize();
		if (isSlotFull) {
			return false;
		}
		boolean isSameItemInSlot = itemInSlot.is(itemStack.getItem()) && itemInSlot.areShareTagsEqual(itemStack);
		if (itemInSlot.isEmpty() || isSameItemInSlot) {
			setItemCopyInSlot(itemStack, slotIndex, amount, isSameItemInSlot);
			itemStack.shrink(amount);
			level.playSound(null, getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1F, 1F);
			return true;
		}
		return false;
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
		return cap == ForgeCapabilities.ITEM_HANDLER ? this.optional.cast() : super.getCapability(cap);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		this.optional.invalidate();
	}

	public LazyOptional<PostboxInventory> getOptional() {
		return this.optional;
	}

	protected void setItemCopyInSlot(ItemStack itemStack, int slotIndex, int amount, boolean adding) {
		ItemStack itemCopy = itemStack.copy();
		itemCopy.setCount(amount);
		if (adding) {
			itemCopy.grow(inventory.getItem(slotIndex).getCount());
		}
		inventory.setItem(slotIndex, itemCopy);
	}

	private boolean canBeOpenedByPlayer(ServerPlayer player) {
		return !isLocked || isPlayerPostman(player);
	}

	private boolean isPlayerPostman(ServerPlayer player) {
		ItemStack helmet = player.getInventory().getArmor(3);
		return helmet != null && helmet.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("superslegend", "mask_postmanshat"));
	}

	private void toggleLockedState(ServerPlayer player) {
		if (isLocked) {
			level.playSound(null, player, SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS, 1F, 1F);
		} else {
			level.playSound(null, player, SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1F, 1F);
		}
		isLocked ^= true;
		String lockStatus = isLocked ? "locked" : "unlocked";
		PlayerChatMessage chatMessage = PlayerChatMessage.unsigned(player.getUUID(), "Postbox " + lockStatus);
		player.createCommandSourceStack().sendChatMessage(new OutgoingChatMessage.Player(chatMessage), false, ChatType.bind(ChatType.CHAT, player));
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
		return new PostboxContainer(pContainerId, pPlayerInventory, this.inventory);
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("container.postbox");
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.put("inventory", inventory.save(new CompoundTag()));
		tag.putBoolean("locked", isLocked);
	}

	@Override
	public void load(@NotNull CompoundTag tag) {
		super.load(tag);
		inventory.load(tag.getCompound("inventory"));
		isLocked = tag.getBoolean("locked");
	}

	public static BlockEntityType<PostboxBlockEntity> createType() {
		return BlockEntityType.Builder.of(PostboxBlockEntity::new, BlockInit.POSTBOX_BLOCK.get()).build(null);
	}
}
