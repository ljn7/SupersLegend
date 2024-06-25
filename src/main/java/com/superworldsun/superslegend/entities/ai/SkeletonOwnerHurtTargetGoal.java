package com.superworldsun.superslegend.entities.ai;

import java.util.EnumSet;

import com.superworldsun.superslegend.interfaces.TameableEntity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class SkeletonOwnerHurtTargetGoal<T extends Monster & TameableEntity> extends TargetGoal
{
	private final T skeleton;
	private LivingEntity ownerLastHurt;
	private int timestamp;

	private final TargetingConditions targetConditions = TargetingConditions.forCombat().ignoreInvisibilityTesting();

	public SkeletonOwnerHurtTargetGoal(T skeleton)
	{
		super(skeleton, false);
		this.skeleton = skeleton;
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	@Override
	public boolean canUse()
	{
		if (this.skeleton.hasOwner())
		{
			LivingEntity livingentity = this.skeleton.getOwner().orElse(null);
			if (livingentity == null)
			{
				return false;
			}
			else
			{
				this.ownerLastHurt = livingentity.getLastHurtMob();
				int i = livingentity.getLastHurtMobTimestamp();
				return i != this.timestamp && this.canAttack(this.ownerLastHurt, this.targetConditions);
			}
		}
		else
		{
			return false;
		}
	}

	@Override
	public void start()
	{
		this.mob.setTarget(this.ownerLastHurt);
		LivingEntity livingentity = this.skeleton.getOwner().orElse(null);
		if (livingentity != null)
		{
			this.timestamp = livingentity.getLastHurtMobTimestamp();
		}

		super.start();
	}
}
