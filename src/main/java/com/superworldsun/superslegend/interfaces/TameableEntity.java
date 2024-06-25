package com.superworldsun.superslegend.interfaces;

import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public interface TameableEntity {
	public Optional<LivingEntity> getOwner();

	public void setOwner(@Nullable LivingEntity owner);

	public Optional<UUID> getOwnerUniqueId();

	public default boolean hasOwner() {
		return getOwner().isPresent();
	}
}
