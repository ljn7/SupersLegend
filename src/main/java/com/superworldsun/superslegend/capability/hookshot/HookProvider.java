package com.superworldsun.superslegend.capability.hookshot;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.network.message.SyncHookshot;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID)
public class HookProvider implements ICapabilitySerializable<CompoundTag>, ICapabilityProvider {
    private HookModel hookModel;
    private static final ResourceLocation HOOK_ID = new ResourceLocation("zelda_hs", "cap_hook");

    public static final Capability<HookModel> HOOK_CAPABILITY = CapabilityManager.get(new CapabilityToken<HookModel>(){});
    private final LazyOptional<HookModel> optional = LazyOptional.of(this::createOrGetHookModel);


    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if (!(event.getObject() instanceof Player)) {
            return;
        }
        event.addCapability(HOOK_ID, new HookProvider());
    }


    @SubscribeEvent
    public static void onPlayerLogin(EntityJoinLevelEvent event)
    {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (event.getEntity().level().isClientSide) {
            return;
        }
        SyncHookshot.send((ServerPlayer)event.getEntity());
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            SyncHookshot.send(serverPlayer);
        }
    }

    public void invalidate() {
        optional.invalidate();
    }

    private HookModel createOrGetHookModel() {
        if(this.hookModel == null) {
            this.hookModel = new HookModel();
        }
        return this.hookModel;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        if (capability == HOOK_CAPABILITY) return optional.cast();
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.createOrGetHookModel().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.createOrGetHookModel().deserializeNBT(nbt);
    }
}
