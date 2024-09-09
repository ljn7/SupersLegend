package com.superworldsun.superslegend.items.item.songs;

import java.util.List;
import java.util.Set;

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

public class AmnesiaSheet extends Item
{
	public AmnesiaSheet()
	{
		super(new Item.Properties().stacksTo(1));
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag)
	{
		super.appendHoverText(stack, world, list, flag);
		list.add(Component.literal("Right click to Forget all learned Songs").withStyle(ChatFormatting.GOLD));
	}

	//TODO add a sound when item is used
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player playerEntity, InteractionHand hand)
	{
		LearnedSongs learnedSongs = LearnedSongs.Provider.getLearnedSongs(playerEntity);
		Set<OcarinaSong> learnedSongsSet = LearnedSongs.Provider.getLearnedSongs(playerEntity).getLearnedSongs();
		
		if (!world.isClientSide)
		{
			playerEntity.sendSystemMessage(Component.translatable("You have forgotten all songs").withStyle(ChatFormatting.DARK_RED));
//			for (OcarinaSong song: learnedSongsSet)
//				learnedSongsSet.remove(song);
			LearnedSongs.Provider.saveLearnedSongs(playerEntity, new LearnedSongs());
			LearnedSongs.Provider.sync((ServerPlayer) playerEntity);
		}
		
		return InteractionResultHolder.success(playerEntity.getItemInHand(hand));
	}
}
