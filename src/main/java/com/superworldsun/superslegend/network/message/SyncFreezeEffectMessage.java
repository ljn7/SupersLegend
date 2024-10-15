package com.superworldsun.superslegend.network.message;

import com.superworldsun.superslegend.effect.FreezeEffect;
import com.superworldsun.superslegend.registries.EffectInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import javax.naming.Context;
import java.io.IOException;
import java.util.function.Supplier;

public class SyncFreezeEffectMessage
{
    public int entityId;

    public SyncFreezeEffectMessage(LivingEntity livingEntity)
    {
        entityId = livingEntity.getId();
    }

    private SyncFreezeEffectMessage()
    {
    }

    public static SyncFreezeEffectMessage decode(FriendlyByteBuf buf)
    {
        SyncFreezeEffectMessage result = new SyncFreezeEffectMessage();
        result.entityId = buf.readInt();
        return result;
    }

    public void encode(FriendlyByteBuf buf)
    {
        buf.writeInt(entityId);
    }

    public static void receive(SyncFreezeEffectMessage message, Supplier<NetworkEvent.Context> ctxSupplier)
    {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.setPacketHandled(true);
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> SyncFreezeEffectMessage.handlePacket(message, ctx)));
    }

    @OnlyIn(Dist.CLIENT)
    public static void handlePacket(SyncFreezeEffectMessage message, NetworkEvent.Context ctx) {
        Minecraft client = Minecraft.getInstance();
        Level level = client.level;
        if (level != null) {
            Entity entity = level.getEntity(message.entityId);
            if (entity != null)
                ((LivingEntity) entity).addEffect(new MobEffectInstance(EffectInit.FREEZE.get(), 2));
        }

    }
}
