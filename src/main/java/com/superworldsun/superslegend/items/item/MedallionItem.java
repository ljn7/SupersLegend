package com.superworldsun.superslegend.items.item;

import com.superworldsun.superslegend.blocks.WarpPadBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public class MedallionItem extends Item {
    private final RegistryObject<WarpPadBlock> warpPadBlockObject;

    public MedallionItem(RegistryObject<WarpPadBlock> warpPadBlockObject) {
        super(new Item.Properties().stacksTo(1));
        this.warpPadBlockObject = warpPadBlockObject;
    }

    public BlockState transformWarpPadState(BlockState blockState) {
        BlockState transformedState = warpPadBlockObject.get().defaultBlockState();
        transformedState = transformedState.setValue(WarpPadBlock.FACING, blockState.getValue(WarpPadBlock.FACING));
        transformedState = transformedState.setValue(WarpPadBlock.BLOCK_PART_X, blockState.getValue(WarpPadBlock.BLOCK_PART_X));
        transformedState = transformedState.setValue(WarpPadBlock.BLOCK_PART_Z, blockState.getValue(WarpPadBlock.BLOCK_PART_Z));
        return transformedState;
    }
}
