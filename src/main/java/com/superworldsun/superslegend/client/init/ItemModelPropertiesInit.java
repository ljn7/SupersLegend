package com.superworldsun.superslegend.client.init;

import com.superworldsun.superslegend.capability.magic.MagicProvider;
import com.superworldsun.superslegend.items.hookshot.HookshotItem;
import com.superworldsun.superslegend.items.hookshot.LongshotItem;
import com.superworldsun.superslegend.registries.ItemInit;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;

public class ItemModelPropertiesInit {
    public static void addCustomItemProperties(){
        makeBow(ItemInit.FAIRY_BOW.get());
        makeBow(ItemInit.HEROS_BOW.get());
        makeBow(ItemInit.LYNEL_BOW_X3.get());
        makeBow(ItemInit.LYNEL_BOW_X5.get());

        NoMagic(ItemInit.DEKU_LEAF.get());
        makeUse(ItemInit.LENS_OF_TRUTH.get());

        makeShield(ItemInit.DEKU_SHIELD.get());
        makeShield(ItemInit.HYLIAN_SHIELD.get());
        makeShield(ItemInit.SACRED_SHIELD.get());

        makeFishingrod(ItemInit.FISHING_ROD.get());

        makeHookshot(ItemInit.HOOKSHOT.get(), "cast", HOOKSHOT_CAST);
        makeHookshot(ItemInit.LONGSHOT.get(), "cast", LONGSHOT_CAST);
    }

    private static void makeBow(Item item) {
        ItemProperties.register(item, new ResourceLocation("pull"), (p_174635_, p_174636_, p_174637_, p_174638_) -> {
            if (p_174637_ == null) {
                return 0.0F;
            } else {
                return p_174637_.getUseItem() != p_174635_ ? 0.0F : (float)(p_174635_.getUseDuration() -
                        p_174637_.getUseItemRemainingTicks()) / 20.0F;
            }
        });

        ItemProperties.register(item, new ResourceLocation("pulling"), (p_174630_, p_174631_, p_174632_, p_174633_) -> {
            return p_174632_ != null && p_174632_.isUsingItem() && p_174632_.getUseItem() == p_174630_ ? 1.0F : 0.0F;
        });
    }

    private static void makeUse(Item item) {
        ItemProperties.register(item, new ResourceLocation("using"), (p_174630_, p_174631_, p_174632_, p_174633_) -> {
            return p_174632_ != null && p_174632_.isUsingItem() && p_174632_.getUseItem() == p_174630_ ? 1.0F : 0.0F;
        });
    }

    private static void makeShield(Item item) {
        ItemProperties.register(item, new ResourceLocation("blocking"), (p_174575_, p_174576_, p_174577_, p_174578_) -> {
            return p_174577_ != null && p_174577_.isUsingItem() && p_174577_.getUseItem() == p_174575_ ? 1.0F : 0.0F;
        });
    }

    private static void NoMagic(Item item) {
        ItemProperties.register(item, new ResourceLocation("no_magic"), (itemStack, clientWorld, livingEntity, seed) -> {
            if (!(livingEntity instanceof Player)) {
                return 0.0F;
            }

            Player player = (Player) livingEntity;
            return !MagicProvider.hasMagic(player, 0.1F) ? 1.0F : 0.0F;
        });
    }

    private static void makeFishingrod(Item item) {
        ItemProperties.register(item, new ResourceLocation("cast"), (p_174585_, p_174586_, p_174587_, p_174588_) -> {
            if (p_174587_ == null) {
                return 0.0F;
            } else {
                boolean flag = p_174587_.getMainHandItem() == p_174585_;
                boolean flag1 = p_174587_.getOffhandItem() == p_174585_;
                if (p_174587_.getMainHandItem().getItem() instanceof FishingRodItem) {
                    flag1 = false;
                }

                return (flag || flag1) && p_174587_ instanceof Player && ((Player)p_174587_).fishing != null ? 1.0F : 0.0F;
            }
        });
    }

    private static void makeHookshot(Item item, String name, ItemPropertyFunction property) {
        ItemProperties.register(item, new ResourceLocation(name), property);
    }

    private static final ItemPropertyFunction HOOKSHOT_CAST = (stack, level, entity, seed) ->
            HookshotItem.SPRITE ? 1.0F : 0.0F;

    private static final ItemPropertyFunction LONGSHOT_CAST = (stack, level, entity, seed) ->
            LongshotItem.LONG_SPRITE ? 1.0F : 0.0F;
}
