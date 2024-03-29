package com.superworldsun.superslegend.items.item;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.entities.projectiles.seeds.*;
import com.superworldsun.superslegend.registries.ItemInit;
import com.superworldsun.superslegend.registries.SoundInit;
import com.superworldsun.superslegend.registries.TagInit;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SlingShot extends BowItem {
    public SlingShot(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.is(TagInit.PELLETS);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            boolean infiniteAmmo = hasInfiniteAmmo(player);
            ItemStack ammoStack = player.getProjectile(stack);
            int charge = getUseDuration(stack) - timeLeft;
            boolean canShoot = !ammoStack.isEmpty() || infiniteAmmo;
            charge = ForgeEventFactory.onArrowLoose(stack, level, player, charge, canShoot);

            if (ammoStack.getItem() == Items.ARROW) {
                ammoStack = new ItemStack(ItemInit.DEKU_SEEDS.get());
            }

            if (charge < 0) {
                return;
            }

            if (canShoot) {
                float shotPower = getPowerForTime(charge) * 0.5f;

                if (shotPower >= 0.1D) {
                    if (!level.isClientSide) {
                        SeedEntity projectile = createAmmoEntity(level, ammoStack);
                        projectile.setOwner(player);
                        projectile.setPos(player.getEyePosition(1F).add(0, -0.1, 0));
                        projectile.shoot(player.getLookAngle(), shotPower * 3F, 0F);
                        level.addFreshEntity(projectile);
                    }

                    float soundPitch = 1f / (player.getRandom().nextFloat() * 0.4f + 1.2f) + shotPower * 0.5f;
                    playSound(player, SoundInit.SLINGSHOT_SHOOT.get(), 1f, soundPitch);

                    if (!infiniteAmmo) {
                        ammoStack.shrink(1);

                        if (ammoStack.isEmpty()) {
                            player.getInventory().removeItem(ammoStack);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    private static boolean hasInfiniteAmmo(Player player) {
        if (player.getAbilities().instabuild) return true;
        return EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY_ARROWS, player) > 0;
    }

    @Nonnull
    private SeedEntity createAmmoEntity(Level world, ItemStack ammoStack) {
        Item ammoItem = ammoStack.getItem();

        if (ammoItem == Items.BEETROOT_SEEDS) {
            return new BeetrootSeedEntity(world);
        } else if (ammoItem == Items.WHEAT_SEEDS) {
            return new WheatSeedEntity(world);
        } else if (ammoItem == Items.MELON_SEEDS) {
            return new MelonSeedEntity(world);
        } else if (ammoItem == Items.PUMPKIN_SEEDS) {
            return new PumpkinSeedEntity(world);
        } else if (ammoItem == Items.COCOA_BEANS) {
            return new CocoaBeanEntity(world);
        }

        return new DekuSeedEntity(world);
    }

    public static float getPowerForTime(int timeInUse) {
        float power = timeInUse / 10f;
        power = (power * power + power * 2f) / 3f;

        if (power > 1f) power = 1f;

        return power;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @SubscribeEvent
    public static void onLivingEntityUseItem(LivingEntityUseItemEvent event) {
        if (event.getItem().getItem() instanceof SlingShot) {
            if (event.getEntity().isUsingItem()) {
                if (event.getDuration() == 72000) {
                    playSound(event.getEntity(), SoundInit.SLINGSHOT_PULL.get());
                }

                if (event.getDuration() > 71980) {
                    event.setDuration(event.getDuration() - 1);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.literal("[Hold Shift for Info]").withStyle(ChatFormatting.DARK_GRAY));
        } else if (Screen.hasShiftDown()) {
            tooltip.add(Component.literal("The slingshot uses seeds as ammo, depending on the seed being used the projectile will be different with unique properties")
                    .withStyle(ChatFormatting.WHITE));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }

    private static void playSound(LivingEntity entity, @NotNull SoundEvent sound) {
        entity.level().playSound(null, entity, sound, SoundSource.PLAYERS, 1f, 1f);
    }

    private static void playSound(LivingEntity entity, @NotNull SoundEvent sound, float volume, float pitch) {
        entity.level().playSound(null, entity, sound, SoundSource.PLAYERS, volume, pitch);
    }
}