package com.superworldsun.superslegend.network.message;

public class DropBombMessage
{
	public DropBombMessage()
	{
	}

	/*public static DropBombMessage decode(FriendlyByteBuf buf)
	{
		return new DropBombMessage();
	}

	public void encode(FriendlyByteBuf buf)
	{
	}

	public static void receive(DropBombMessage message, Supplier<NetworkEvent.Context> ctxSupplier)
	{
		NetworkEvent.Context ctx = ctxSupplier.get();
		ServerPlayer player = ctx.getSender();
		ctx.setPacketHandled(true);
		ItemStack bombBag = CuriosApi.getCuriosHelper().findEquippedCurio(stack -> stack.getItem() instanceof BombBagItem, player).map(ImmutableTriple::getRight).orElse(ItemStack.EMPTY);

		if (bombBag.isEmpty())
			return;

		BombBagItem bombBagItem = (BombBagItem) bombBag.getItem();
		Pair<ItemStack, Integer> bagContents = bombBagItem.getContents(bombBag);
		int bombsCount = bagContents.getRight();

		if (bombsCount == 0)
			return;

		InteractionHand emptyHand;

		if (player.getMainHandItem().isEmpty())
		{
			emptyHand = InteractionHand.MAIN_HAND;
		}
		else if (player.getOffhandItem().isEmpty())
		{
			emptyHand = InteractionHand.OFF_HAND;
		}
		else
		{
			player.sendMessage(new StringTextComponent("You'll need to free your hands for that"), null);
			return;
		}

		player.setItemInHand(emptyHand, new ItemStack(bagContents.getKey().getItem()));
		bombBagItem.setCount(bombBag, bombsCount - 1);
	}*/
}