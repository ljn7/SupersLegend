package com.superworldsun.superslegend.entities.ai;

import java.util.EnumSet;

import com.superworldsun.superslegend.interfaces.TameableEntity;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import javax.annotation.Nullable;

public class SkeletonOwnerHurtByTargetGoal<T extends Monster & TameableEntity> extends TargetGoal
{
	private final T skeleton;
	private LivingEntity ownerLastHurtBy;
	private int timestamp;

	private final static TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

	public SkeletonOwnerHurtByTargetGoal(T skeleton)
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
				this.ownerLastHurtBy = livingentity.getLastHurtByMob();
				int i = livingentity.getLastHurtByMobTimestamp();
				return i != this.timestamp && this.canAttack(this.ownerLastHurtBy, HURT_BY_TARGETING);
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
		this.mob.setTarget(this.ownerLastHurtBy);
		LivingEntity livingentity = this.skeleton.getOwner().orElse(null);
		if (livingentity != null)
		{
			this.timestamp = livingentity.getLastHurtByMobTimestamp();
		}
		super.start();
	}

	@Override
	protected boolean canAttack(@Nullable LivingEntity pPotentialTarget, TargetingConditions pTargetPredicate) {
		if (pPotentialTarget == null) {
			return false;
		} else if (!this.test(this.mob, pPotentialTarget)) {
			return false;
		} else if (!this.mob.isWithinRestriction(pPotentialTarget.blockPosition())) {
			return false;
		} else {
			return true;
		}
	}

	private boolean test(@Nullable LivingEntity pAttacker, LivingEntity pTarget) {
		if (pAttacker == pTarget) {
			return false;
		} else if (!pTarget.canBeSeenByAnyone()) {
			return false;
		} else {
			if (pAttacker == null) {
				if (!pTarget.canBeSeenAsEnemy() || pTarget.level().getDifficulty() == Difficulty.PEACEFUL) {
					return false;
				}
			} else {
				if (!pAttacker.canAttackType(pTarget.getType())) {
					return false;
				}
			}
			return true;
		}
	}
}
