package com.superworldsun.superslegend.network.message;

import com.superworldsun.superslegend.interfaces.JumpingEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DoubleJumpMessage
{
	public DoubleJumpMessage()
	{
	}

	public static DoubleJumpMessage decode(FriendlyByteBuf buf)
	{
		DoubleJumpMessage result = new DoubleJumpMessage();
		return result;
	}

	public void encode(FriendlyByteBuf buf)
	{
	}

	public static void receive(DoubleJumpMessage message, Supplier<NetworkEvent.Context> ctxSupplier)
	{
		ServerPlayer player = ctxSupplier.get().getSender();

		if (player != null)
		{
			ctxSupplier.get().enqueueWork(() -> ((JumpingEntity) player).doubleJump());
		}

		ctxSupplier.get().setPacketHandled(true);
	}
}