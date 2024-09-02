package com.superworldsun.superslegend.items.block;

import javax.annotation.Nullable;

import com.superworldsun.superslegend.blocks.ShadowBlock;
import com.superworldsun.superslegend.client.render.bewlr.ShadowBlockItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Block;
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
            if (!(clickedBlockState.getBlock() instanceof ShadowBlock)) {
                saveDisguiseInStack(context.getItemInHand(), clickedBlockState);
                level.playSound(null, context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(),
                        SoundEvents.LODESTONE_PLACE, SoundSource.PLAYERS, 0.2F,
                        ((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }

    public static void saveDisguiseInStack(ItemStack itemStack, @Nullable BlockState disguise) {
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putInt("disguise", Block.getId(disguise));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (player.isShiftKeyDown() && itemStack.hasTag()) {
            itemStack.setTag(null);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.LODESTONE_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F + level.random.nextFloat() * 0.4F);
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