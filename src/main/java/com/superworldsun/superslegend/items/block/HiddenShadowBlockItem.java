package com.superworldsun.superslegend.items.block;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.registries.BlockInit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = SupersLegendMain.MOD_ID)
public class HiddenShadowBlockItem extends ShadowBlockBaseItem {
    public HiddenShadowBlockItem() {
        super(BlockInit.HIDDEN_SHADOW_BLOCK.get(), new Item.Properties().stacksTo(64));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.literal("This Block remains unseen by the unobservant").withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltipComponents.add(Component.literal("Look at a block and Sneak+RightClick").withStyle(ChatFormatting.DARK_PURPLE));
        tooltipComponents.add(Component.literal("to copy its look.").withStyle(ChatFormatting.DARK_PURPLE));
        tooltipComponents.add(Component.literal("Sneak+Right-Click the air to remove copy").withStyle(ChatFormatting.DARK_PURPLE));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }
}
