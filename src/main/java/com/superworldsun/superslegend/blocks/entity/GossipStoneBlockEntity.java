package com.superworldsun.superslegend.blocks.entity;

import com.superworldsun.superslegend.registries.BlockEntityInit;
import com.superworldsun.superslegend.registries.BlockInit;
import com.superworldsun.superslegend.registries.ItemInit;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import top.theillusivec4.curios.api.CuriosApi;

public class GossipStoneBlockEntity extends BlockEntity {
    private String message = "";

    public GossipStoneBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.GOSSIP_STONE.get(), pos, state);
    }

    @Override
    public void load(CompoundTag compound) {
        message = compound.getString("message");
        super.load(compound);
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        compound.putString("message", message);
        super.saveAdditional(compound);
    }

    public Component getMessage(Player player) {
        boolean hasMaskOfTruth = CuriosApi.getCuriosHelper().findEquippedCurio(ItemInit.MASK_MASKOFTRUTH.get(), player).isPresent();
        if (message.isEmpty() || !hasMaskOfTruth) {
            return Component.translatable("block.superslegend.gossip_stone_block.silent").withStyle(ChatFormatting.BLACK,
                    ChatFormatting.BOLD);
        } else {
            return Component.literal(message).withStyle(ChatFormatting.GRAY,
                    ChatFormatting.BOLD);
        }
    }

    public void setMessage(String message) {
        this.message = message;
        setChanged();
    }

    public static BlockEntityType<GossipStoneBlockEntity> createType() {
        return BlockEntityType.Builder.of(GossipStoneBlockEntity::new, BlockInit.GOSSIP_STONE_BLOCK.get()).build(null);
    }
}