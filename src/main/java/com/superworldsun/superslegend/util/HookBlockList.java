package com.superworldsun.superslegend.util;

import com.superworldsun.superslegend.registries.BlockInit;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class HookBlockList {

    /**
     * Here you write the list of blocks to which the hook can be hooked. It is easy to add blocks.
     */
    public static List<Block> hookableBlocks = new ArrayList<>();

    public static void setHookableBlocks() {
        hookableBlocks.clear();
        hookableBlocks.add(BlockInit.GRAPPLE_BLOCK.get());

        // Include wooden blocks using tags
        for (Block block : ForgeRegistries.BLOCKS) {
            if (block.defaultBlockState().is(BlockTags.PLANKS) ||
                    block.defaultBlockState().is(BlockTags.LOGS) ||
                    block.defaultBlockState().is(BlockTags.WOODEN_FENCES)) {
                hookableBlocks.add(block);
            }
        }
    }
    static {
        setHookableBlocks();
    }
}