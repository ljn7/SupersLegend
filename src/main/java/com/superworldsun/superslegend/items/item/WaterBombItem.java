package com.superworldsun.superslegend.items.item;

import com.superworldsun.superslegend.entities.projectiles.bombs.WaterBombEntity;
import com.superworldsun.superslegend.items.customclass.NonEnchantItem;
import com.superworldsun.superslegend.registries.EntityTypeInit;
import com.superworldsun.superslegend.registries.SoundInit;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class WaterBombItem extends NonEnchantItem {
    // Bomb rendering, entity and logic code credited to Spelunkcraft contributor ntfwc
    public WaterBombItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack bombStack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if (player.isShiftKeyDown()) {
                WaterBombEntity bomb = new WaterBombEntity(EntityTypeInit.WATER_BOMB.get(), level);
                bomb.setPos(player.getX(), player.getY(), player.getZ());
                level.playSound(null, player.blockPosition(), SoundInit.BOMB_FUSE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                level.addFreshEntity(bomb);
            } else {
                WaterBombEntity waterBombEntity = new WaterBombEntity(player, level);
                float pitch = 0;
                float throwingForce = 0.7F;
                waterBombEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), pitch, throwingForce, 0.9F);
                level.playSound(null, player.blockPosition(), SoundInit.BOMB_FUSE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                level.addFreshEntity(waterBombEntity);
            }
            if (!player.isCreative()) {
                bombStack.shrink(1);
            }
        }

        return InteractionResultHolder.consume(bombStack);
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.literal("Use this on submerged blocks to destroy them").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.literal("Right-Click to throw").withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.literal("Sneak+Right-Click to Drop Bomb").withStyle(ChatFormatting.GREEN));
    }
}
