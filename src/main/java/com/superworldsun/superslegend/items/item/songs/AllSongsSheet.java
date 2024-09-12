package com.superworldsun.superslegend.items.item.songs;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;


import com.superworldsun.superslegend.registries.OcarinaSongInit;
import com.superworldsun.superslegend.songs.LearnedSongs;
import com.superworldsun.superslegend.songs.OcarinaSong;


import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AllSongsSheet extends Item
{
	public AllSongsSheet()
	{
		super(new Item.Properties().stacksTo(1));
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag)
	{
		super.appendHoverText(stack, world, list, flag);
		list.add(Component.literal("[Creative Item Only]").withStyle(ChatFormatting.YELLOW));
		list.add(Component.literal("Right click to Learn All Songs").withStyle(ChatFormatting.GOLD));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player playerEntity, InteractionHand hand)
	{
		LearnedSongs learnedSongs = LearnedSongs.Provider.getLearnedSongs(playerEntity);
		Set<OcarinaSong> learnedSongsSet = learnedSongs.getLearnedSongs();
		if (!world.isClientSide)
		{
			playerEntity.sendSystemMessage(Component.translatable("You have learned all available songs").withStyle(ChatFormatting.DARK_GREEN));
			for (Supplier<OcarinaSong> songSupplier : OcarinaSongInit.getAllSongSuppliers()) {
                learnedSongsSet.add(songSupplier.get());
			};
			LearnedSongs.Provider.saveLearnedSongs(playerEntity, learnedSongs);
			LearnedSongs.Provider.sync((ServerPlayer) playerEntity);
		}

		return InteractionResultHolder.success(playerEntity.getItemInHand(hand));
	}
}
