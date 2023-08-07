package com.superworldsun.superslegend.events;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.api.DamageReductionItem;

import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID)
public class ItemEvents {
	/**
	 * Applies damage reduction from all instances of {@link DamageReductionItem}
	 */
	@SubscribeEvent
	public static void applyDamageReductionBonus(LivingHurtEvent event) {
		CuriosApi.getCuriosHelper().findCurios(event.getEntity(), stack -> stack.getItem() instanceof DamageReductionItem).forEach(slot -> {
			DamageReductionItem item = (DamageReductionItem) slot.stack().getItem();
			if (item.canReduceDamage(event.getSource())) {
				event.setAmount(event.getAmount() * (1 - item.getDamageReduction()));
			}
		});
		event.getEntity().getArmorSlots().forEach(stack -> {
			if (stack.getItem() instanceof DamageReductionItem) {
				DamageReductionItem item = (DamageReductionItem) stack.getItem();
				if (item.canReduceDamage(event.getSource())) {
					event.setAmount(event.getAmount() * (1 - item.getDamageReduction()));
				}
			}
		});
		if (event.getAmount() <= 0) event.setCanceled(true);
	}
}
