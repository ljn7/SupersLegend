package com.superworldsun.superslegend.items.curios.hands;

import com.superworldsun.superslegend.SupersLegendMain;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID)
public class GoldenGauntlets extends StrengthHandItem {
	public GoldenGauntlets() {
		super(new Properties().stacksTo(1), 3);
	}
}