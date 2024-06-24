package com.superworldsun.superslegend.events;

import com.google.common.base.Predicates;
import com.superworldsun.superslegend.SupersLegendMain;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = SupersLegendMain.MOD_ID)
public class PlayerMobPickupEvents {
	// Prevents attacking when a mob is held
	@SubscribeEvent
	public static void onPlayerAttack(AttackEntityEvent event) {
		Player player = event.getEntity();
		if (!player.getPassengers().isEmpty()) {
			event.setCanceled(true);
		}
	}

	// Prevents item interaction when a mob is held
	@SubscribeEvent
	public static void onPlayerInteractItem(PlayerInteractEvent.RightClickItem event) {
		Player player = event.getEntity();
		if (!player.getPassengers().isEmpty()) {
			event.setCanceled(true);
		}
	}

	// Makes the player fall slowly if holding a chicken
	@SubscribeEvent
	public static void onLivingTick(LivingEvent.LivingTickEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			boolean hasChickenPassenger = player.getPassengers().stream().anyMatch(Predicates.instanceOf(Chicken.class));
			if (hasChickenPassenger) {
				player.fallDistance = 0F;
				// slows fall speed
				if (player.getDeltaMovement().y < -0.1) {
					Vec3 movement = player.getDeltaMovement();
					player.setDeltaMovement(new Vec3(movement.x, -0.08, movement.z));
				}
			}
		}
	}

	// Removes the player's first-person hand when holding a mob
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onRenderHand(RenderHandEvent event) {
		Minecraft minecraft = Minecraft.getInstance();
		Player player = minecraft.player;
		if (player != null && !player.getPassengers().isEmpty()) {
			event.setCanceled(true);
		}
	}

	//TODO, this dosent seem to update properly when the entity takes damage,
	// the entity will take damage and will show them still riding the player but will actually have dismounted and docent show until an update happens

	// Makes the entity dismount the player if it takes damage
	/*@SubscribeEvent
	public static void onEntityHurt(LivingHurtEvent event) {
		Entity entity = event.getEntity();
		if (entity.getVehicle() instanceof Player) {
			entity.stopRiding();
		}
	}*/
}
