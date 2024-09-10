package com.superworldsun.superslegend.items.block;

import javax.annotation.Nullable;

import com.superworldsun.superslegend.blocks.*;
import com.superworldsun.superslegend.client.render.bewlr.ShadowBlockItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ShadowBlockBaseItem extends BlockItem {
    protected ShadowBlockBaseItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            Level level = context.getLevel();

            BlockState clickedBlockState = level.getBlockState(context.getClickedPos());
            boolean clickedShadowBlock = clickedBlockState.getBlock() instanceof ShadowBlock;
            boolean clickedFullBlock = Block.isShapeFullBlock(clickedBlockState.getShape(context.getLevel(), context.getClickedPos()));

            if ( clickedShadowBlock || avoidingBlocks(clickedBlockState.getBlock()) ||
                    ( !clickedFullBlock && !allowedBlocks(clickedBlockState.getBlock() ) )) {
                return InteractionResult.FAIL;
            }

            saveDisguiseInStack(context.getItemInHand(), clickedBlockState);
            level.playSound(context.getPlayer(), context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(),
                    SoundEvents.LODESTONE_PLACE, SoundSource.PLAYERS, 1F, 1F);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    private boolean allowedBlocks(Block block) {
        return block instanceof StairBlock || block instanceof SlabBlock;
    }

    private boolean avoidingBlocks(Block block) {
      return block instanceof DoublePlantBlock
              || block instanceof TallGrassBlock
              || block instanceof FlowerBlock
              || block instanceof TallFlowerBlock
              || block instanceof TallSeagrassBlock
              || block instanceof KelpPlantBlock;
    };

    public static void saveDisguiseInStack(ItemStack itemStack, @Nullable BlockState disguise) {
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putInt("disguise", Block.getId(disguise));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (player.isShiftKeyDown() && itemStack.hasTag()) {
            itemStack.setTag(null);
            level.playSound(player, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.LODESTONE_BREAK, SoundSource.PLAYERS, 1F, 1F);
            return InteractionResultHolder.success(itemStack);
        }
        return super.use(level, player, usedHand);
    }

    @Nullable
    public static BlockState loadDisguiseFromStack(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        if (tag != null && tag.contains("disguise")) {
            return Block.stateById(tag.getInt("disguise"));
        }
        return null;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(ShadowBlockClientItemExtension.INSTANCE);
    }

    static class ShadowBlockClientItemExtension implements IClientItemExtensions {

        public static ShadowBlockClientItemExtension INSTANCE = new ShadowBlockClientItemExtension();

        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return new ShadowBlockItemRenderer();
        }
    }
}