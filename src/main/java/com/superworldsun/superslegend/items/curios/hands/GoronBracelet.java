package com.superworldsun.superslegend.items.curios.hands;

import com.superworldsun.superslegend.SupersLegendMain;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID)
public class GoronBracelet extends StrengthHandItem {
	public GoronBracelet() {
		super(new Properties().stacksTo(1), 1);
	}
}