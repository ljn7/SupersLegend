package com.superworldsun.superslegend.effect;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.client.render.layer.FreezeEffectLayer;
import com.superworldsun.superslegend.network.NetworkDispatcher;
import com.superworldsun.superslegend.network.message.SyncFreezeEffectMessage;
import com.superworldsun.superslegend.registries.EffectInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID)
public class FreezeEffect extends MobEffect {
    public FreezeEffect() {
        super(MobEffectCategory.HARMFUL, 0xD6FFFF);
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.hasEffect(EffectInit.FREEZE.get())) {
            MobEffectInstance freezeEffect = entity.getEffect(EffectInit.FREEZE.get());
            if (entity.isDeadOrDying() || freezeEffect.getDuration() == 0) {
                entity.removeEffect(EffectInit.FREEZE.get());
                return;
            }
            if (!entity.level().isClientSide()) {
                NetworkDispatcher.network_channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new SyncFreezeEffectMessage(entity));
            }
            entity.baseTick();
            entity.move(MoverType.SELF, entity.getDeltaMovement());
            entity.setDeltaMovement(entity.getDeltaMovement().scale(0.8));
            entity.move(MoverType.SELF, new Vec3(0, -0.4, 0));
            event.setCanceled(true);
        }
    }

    private static final List<LivingEntityRenderer<?, ?>> MODIFIED_RENDERERS = new ArrayList<>();

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onLivingRender(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        if (!event.getEntity().hasEffect(EffectInit.FREEZE.get()))
            return;

        LivingEntity entity = event.getEntity();
        entity.xOld = entity.getX();
        entity.yOld = entity.getY();
        entity.zOld = entity.getZ();
        entity.yRotO = entity.getYRot();
        entity.calculateEntityAnimation(false);
        var renderer = event.getRenderer();
        if (!MODIFIED_RENDERERS.contains(renderer)) {
            renderer.addLayer(new FreezeEffectLayer(renderer));
            MODIFIED_RENDERERS.add(event.getRenderer());
        }
    }

//    @SubscribeEvent
//    public static void onEntityRendererRegister(EntityRenderersEvent.AddLayers event) {
//        ForgeRegistries.ENTITY_TYPES.getValues().forEach(entityType -> {
//            EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
//            var renderer = entityRenderDispatcher.renderers.get(entityType);
//            if (renderer instanceof LivingEntityRenderer livingRenderer) {
//                livingRenderer.addLayer(new FreezeEffectLayer(livingRenderer));
//            }
//        });
//    }
}