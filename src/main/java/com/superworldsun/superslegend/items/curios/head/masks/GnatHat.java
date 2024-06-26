package com.superworldsun.superslegend.items.curios.head.masks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

import java.util.List;

public class GnatHat extends Item implements ICurioItem {
    public GnatHat(Properties pProperties) {
        super(pProperties);
    }

    //TODO, would be funny to make it so animals such as foxes and chickens would attack the player when small, low prio.

    //TODO Make sure stats are fine tuned
    //TODO Find a way to have the player smoothly transition into their new scales
    @Override
    public void onEquip(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        ICurioItem.super.onEquip(identifier, index, livingEntity, stack);
        ScaleData height = ScaleTypes.HEIGHT.getScaleData(livingEntity);
        height.setScale(0.1f);
        ScaleData width = ScaleTypes.WIDTH.getScaleData(livingEntity);
        width.setScale(0.1f);
        ScaleData reach = ScaleTypes.BLOCK_REACH.getScaleData(livingEntity);
        reach.setScale(0.4f);
        ScaleData ereach = ScaleTypes.ENTITY_REACH.getScaleData(livingEntity);
        ereach.setScale(0.4f);
        ScaleData motion = ScaleTypes.MOTION.getScaleData(livingEntity);
        motion.setScale(0.25f);
        ScaleData jump = ScaleTypes.JUMP_HEIGHT.getScaleData(livingEntity);
        jump.setScale(1f);
        ScaleData step = ScaleTypes.STEP_HEIGHT.getScaleData(livingEntity);
        step.setScale(1f);
        ScaleData mining = ScaleTypes.MINING_SPEED.getScaleData(livingEntity);
        mining.setScale(0.1f);
    }

    @Override
    public void onUnequip(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        ICurioItem.super.onUnequip(identifier, index, livingEntity, stack);
        ScaleData height = ScaleTypes.HEIGHT.getScaleData(livingEntity);
        height.resetScale();
        ScaleData width = ScaleTypes.WIDTH.getScaleData(livingEntity);
        width.resetScale();
        ScaleData reach = ScaleTypes.BLOCK_REACH.getScaleData(livingEntity);
        reach.resetScale();
        ScaleData ereach = ScaleTypes.ENTITY_REACH.getScaleData(livingEntity);
        ereach.resetScale();
        ScaleData motion = ScaleTypes.MOTION.getScaleData(livingEntity);
        motion.resetScale();
        ScaleData jump = ScaleTypes.JUMP_HEIGHT.getScaleData(livingEntity);
        jump.resetScale();
        ScaleData step = ScaleTypes.STEP_HEIGHT.getScaleData(livingEntity);
        step.resetScale();
        ScaleData mining = ScaleTypes.MINING_SPEED.getScaleData(livingEntity);
        mining.resetScale();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.literal("Shrink down to the size of a Gnat").withStyle(ChatFormatting.WHITE));
        tooltip.add(Component.literal("You wont be as strong shrunk down").withStyle(ChatFormatting.RED));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
