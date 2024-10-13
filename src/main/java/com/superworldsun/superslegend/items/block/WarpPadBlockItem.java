package com.superworldsun.superslegend.items.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class WarpPadBlockItem extends BlockItem {
    public WarpPadBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext itemUseContext, BlockState blockState) {
        Level level = itemUseContext.getLevel();
        BlockPos blockPos = itemUseContext.getClickedPos();

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos blockPartPos = blockPos.offset(x, 0, z);

                if (!level.getBlockState(blockPartPos).canBeReplaced(itemUseContext)) {
                    return false;
                }
            }
        }

        return super.canPlace(itemUseContext, blockState);
    }
}