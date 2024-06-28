package com.superworldsun.superslegend.blocks.entity;

import com.superworldsun.superslegend.registries.BlockInit;
import com.superworldsun.superslegend.registries.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.ItemStackHandler;

public class PedestalBlockEntity extends BlockEntity {
	private final ItemStackHandler inventory = new ItemStackHandler(1);

	public PedestalBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityInit.PEDESTAL_ENTITY.get(), pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		compound.put("inventory", inventory.serializeNBT());
		super.saveAdditional(compound);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		inventory.deserializeNBT(compound.getCompound("inventory"));
	}

	@Override
	public AABB getRenderBoundingBox() {
		return new AABB(worldPosition, worldPosition.offset(1, 1, 1).above());
	}

	public ItemStack getSword() {
		return inventory.getStackInSlot(0);
	}

	public void setSword(ItemStack stack) {
		inventory.setStackInSlot(0, stack);
		setChanged();
	}

	public void dropSword() {
		ItemEntity itemEntity = new ItemEntity(level, worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, getSword());
		level.addFreshEntity(itemEntity);
	}

	public static BlockEntityType<PedestalBlockEntity> createType() {
		return BlockEntityType.Builder.of(PedestalBlockEntity::new, BlockInit.PEDESTAL.get()).build(null);
	}
}
