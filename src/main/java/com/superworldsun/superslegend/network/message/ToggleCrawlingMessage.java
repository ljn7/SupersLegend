package com.superworldsun.superslegend.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleCrawlingMessage {
	public ToggleCrawlingMessage() {
	}

	public static ToggleCrawlingMessage decode(FriendlyByteBuf buf) {
		return new ToggleCrawlingMessage();
	}

	public void encode(FriendlyByteBuf buf) {
	}

	public static void receive(ToggleCrawlingMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		ServerPlayer player = ctx.getSender();
		ctx.setPacketHandled(true);
		if (player.getForcedPose() != Pose.SWIMMING)
			player.setForcedPose(Pose.SWIMMING);
		else
			player.setForcedPose(null);
	}
}
