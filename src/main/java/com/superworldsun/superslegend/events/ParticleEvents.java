package com.superworldsun.superslegend.events;

import com.superworldsun.superslegend.SupersLegendMain;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID)
public class ParticleEvents {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerParticles(RegisterParticleProvidersEvent event) {
//        Minecraft.getInstance().particleEngine.register(ParticleTypes.BLOCK,
//                (spriteSet) -> (particleOptions, level, x, y, z, xSpeed, ySpeed, zSpeed) -> {
//                    if (particleOptions instanceof BlockParticleOption) {
//                        BlockParticleOption blockParticleOption = particleOptions;
//                        BlockPos pos = new BlockPos((int)x, (int)y, (int)z);
//                        BlockEntity blockEntity = level.getBlockEntity(pos);
//                        if (blockEntity instanceof ShadowBlockEntity) {
//                            ShadowBlockEntity shadowTile = (ShadowBlockEntity) blockEntity;
//                            BlockState appearance = shadowTile.getAppearance();
//                            return new TerrainParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, appearance)
//                                    .updateSprite(appearance, pos);
//                        }
//                    }
//                    return null;
//                }
//        );
    }
}
