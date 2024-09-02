package com.superworldsun.superslegend.blocks.util;

import com.superworldsun.superslegend.blocks.entity.ShadowBlockEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ShadowBlockParticleProvider implements ParticleProvider<BlockParticleOption> {
    public ShadowBlockParticleProvider(SpriteSet spriteSet) {}

    @Override
    public Particle createParticle(BlockParticleOption particleOptions, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        BlockPos pos = new BlockPos((int)x, (int)y, (int)z);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ShadowBlockEntity) {
            ShadowBlockEntity shadowTile = (ShadowBlockEntity) blockEntity;
            BlockState appearance = shadowTile.getAppearance();
            return new TerrainParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, appearance)
                    .updateSprite(appearance, pos);
        }
        return null;
    }
}