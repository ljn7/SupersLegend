package com.superworldsun.superslegend.entities.ai;

import com.superworldsun.superslegend.interfaces.IMaskAbility;
import com.superworldsun.superslegend.registries.ItemInit;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.EnumSet;

public class FollowBremenMaskGoal extends Goal
{
	private static final TargetingConditions TEMP_TARGETING = TargetingConditions.forNonCombat().range(10.0D).ignoreInvisibilityTesting().ignoreLineOfSight();
	protected final Animal mob;
	private final double speedModifier;
	private double px;
	private double py;
	private double pz;
	private double pRotX;
	private double pRotY;
	protected Player player;
	private int calmDown;
	private boolean isRunning;
	private final boolean canScare;
	
	public FollowBremenMaskGoal(Animal mob, double speed, boolean canScare)
	{
		this.mob = mob;
		this.speedModifier = speed;
		this.canScare = canScare;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		
		if (!(mob.getNavigation() instanceof GroundPathNavigation) && !(mob.getNavigation() instanceof FlyingPathNavigation))
		{
			throw new IllegalArgumentException("Unsupported mob type '" + mob.getType().toString() + "'");
		}
	}
	
	public boolean canUse()
	{
		if (this.calmDown > 0)
		{
			--this.calmDown;
			return false;
		}
		else
		{
			this.player = this.mob.level().getNearestPlayer(TEMP_TARGETING, this.mob);
			if (this.player == null)
			{
				return false;
			}
			else
			{
				return shouldFollow(player);
			}
		}
	}
	
	private boolean shouldFollow(Player player)
	{
		ItemStack stack0 = CuriosApi.getCuriosHelper().findEquippedCurio(ItemInit.MASK_BREMANMASK.get(), player).map(ImmutableTriple::getRight).orElse(ItemStack.EMPTY);
		if (!stack0.isEmpty())
		return stack0.getItem() == ItemInit.MASK_BREMANMASK.get() && ((IMaskAbility) stack0.getItem()).isPlayerUsingAbility(player);
		return false;
	}
	
	public boolean canContinueToUse()
	{
		if (this.canScare())
		{
			if (this.mob.distanceToSqr(this.player) < 36.0D)
			{
				if (this.player.distanceToSqr(this.px, this.py, this.pz) > 0.010000000000000002D)
				{
					return false;
				}
				
				if (Math.abs((double) this.player.xRotO - this.pRotX) > 5.0D || Math.abs((double) this.player.yRotO - this.pRotY) > 5.0D)
				{
					return false;
				}
			}
			else
			{
				this.px = this.player.getX();
				this.py = this.player.getY();
				this.pz = this.player.getZ();
			}
			
			this.pRotX = (double) this.player.xRotO;
			this.pRotY = (double) this.player.yRotO;
		}
		
		return this.canUse();
	}
	
	protected boolean canScare()
	{
		return this.canScare;
	}
	
	public void start()
	{
		this.px = this.player.getX();
		this.py = this.player.getY();
		this.pz = this.player.getZ();
		this.isRunning = true;
	}
	
	public void stop()
	{
		this.player = null;
		this.mob.getNavigation().stop();
		this.calmDown = 100;
		this.isRunning = false;
	}
	
	public void tick()
	{
		this.mob.getLookControl().setLookAt(this.player, (float) (this.mob.getMaxHeadYRot() + 20), (float) this.mob.getMaxHeadXRot());
		if (this.mob.distanceToSqr(this.player) < 6.25D)
		{
			this.mob.getNavigation().stop();
		}
		else
		{
			this.mob.getNavigation().moveTo(this.player, this.speedModifier);
		}
		
	}
	
	public boolean isRunning()
	{
		return this.isRunning;
	}
}
