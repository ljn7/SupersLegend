package com.superworldsun.superslegend.items.curios.head.masks;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.client.sound.BremenMaskSound;
import com.superworldsun.superslegend.entities.ai.FollowBremenMaskGoal;
import com.superworldsun.superslegend.interfaces.IMaskAbility;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = SupersLegendMain.MOD_ID)
public class BremenMask extends Item implements IMaskAbility, ICurioItem {
    public BremenMask(Properties pProperties) {
        super(pProperties);
    }

    // Adds goal for following players with Bremen mask into every animal
    @SubscribeEvent
    public static void onEntityConstructing(EntityJoinLevelEvent event)
    {
        if (event.getEntity() instanceof Animal)
        {
            Animal animal = (Animal) event.getEntity();

            if (!(animal.getNavigation() instanceof GroundPathNavigation) && !(animal.getNavigation() instanceof FlyingPathNavigation))
            {
                return;
            }

            animal.goalSelector.addGoal(3, new FollowBremenMaskGoal(animal, 1.2D, false));
        }
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack)
    {
        if (livingEntity.getType() != EntityType.PLAYER)
            return;

        if (isPlayerUsingAbility((Player) livingEntity) && livingEntity.isSprinting())
        {
            livingEntity.setSprinting(false);
        }
    }

    @Override
    public void startUsingAbility(Player player)
    {
        if (player.level().isClientSide)
        {
            playMaskSound(player);
        }

        UUID slowId = UUID.fromString("7176f8ab-df6b-4065-9232-3c314fadb655");
        // -0.3 is 30% slower
        AttributeModifier modifier = new AttributeModifier(slowId, "Bremen Mask Slow", -0.3, AttributeModifier.Operation.MULTIPLY_BASE);
        AttributeInstance movespeed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        movespeed.addTransientModifier(modifier);
        IMaskAbility.super.startUsingAbility(player);
    }

    @Override
    public void stopUsingAbility(Player player)
    {
        UUID slowId = UUID.fromString("7176f8ab-df6b-4065-9232-3c314fadb655");
        AttributeModifier modifier = player.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(slowId);
        AttributeInstance movespeed = player.getAttribute(Attributes.MOVEMENT_SPEED);

        if (modifier != null)
        {
            movespeed.removeModifier(modifier);
        }

        IMaskAbility.super.stopUsingAbility(player);
    }

    @OnlyIn(Dist.CLIENT)
    private void playMaskSound(Player player)
    {
        Minecraft client = Minecraft.getInstance();
        client.getSoundManager().play(new BremenMaskSound(player));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.literal("A mask animals would love!").withStyle(ChatFormatting.WHITE));
        //tooltip.add(Component.literal("Hold '" + keybind + "' to have animals follow you").withStyle(ChatFormatting.GREEN));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
