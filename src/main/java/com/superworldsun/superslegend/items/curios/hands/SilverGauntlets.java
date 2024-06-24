package com.superworldsun.superslegend.items.curios.hands;

import com.superworldsun.superslegend.SupersLegendMain;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID)
public class SilverGauntlets extends StrengthHandItem {
	public SilverGauntlets() {
		super(new Properties(), 2);
	}
}