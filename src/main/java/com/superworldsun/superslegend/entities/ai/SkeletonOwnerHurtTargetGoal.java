package com.superworldsun.superslegend.entities.ai;

import java.util.EnumSet;

import com.superworldsun.superslegend.interfaces.TameableEntity;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import javax.annotation.Nullable;

public class SkeletonOwnerHurtTargetGoal<T extends Monster & TameableEntity> extends TargetGoal
{
	private final T skeleton;
	private LivingEntity ownerLastHurt;
	private int timestamp;

	private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

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
				return i != this.timestamp && this.canAttack(this.ownerLastHurt, HURT_BY_TARGETING);
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

	@Override
	protected boolean canAttack(@Nullable LivingEntity pPotentialTarget, TargetingConditions pTargetPredicate) {
		LivingEntity skeletonOwner = this.skeleton.getOwner().orElse(null);
		if (pPotentialTarget == null) {
			return false;
		} else if (!this.test(this.mob, pPotentialTarget)) {
			return false;
		} else if (!this.mob.isWithinRestriction(pPotentialTarget.blockPosition())) {
			return false;
		} else if (skeletonOwner != null && pPotentialTarget instanceof Wolf wolf) {
			LivingEntity wolfOwner = wolf.getOwner();
			if (wolfOwner != null
					&& wolfOwner.getUUID().equals(skeletonOwner.getUUID())) {
				return false;
			}
		} else if (skeletonOwner != null && pPotentialTarget instanceof AbstractSkeleton abstractSkeleton) {
			LivingEntity targetSkeletonOwner = ((TameableEntity) abstractSkeleton).getOwner().orElse(null);
			if (targetSkeletonOwner != null
				&& targetSkeletonOwner.getUUID().equals(skeletonOwner.getUUID())) {
				return  false;
			}
		}
		return true;
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
