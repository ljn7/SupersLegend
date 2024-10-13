package com.superworldsun.superslegend.warppads;

import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.network.NetworkDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = SupersLegendMain.MOD_ID)
public class WarpPadsStorage {
    private static final String NBT_KEY = "WarpPadsData";
    private static final Map<UUID, Map<Block, BlockPos>> playerWarpPads = new HashMap<>();

    public static Optional<BlockPos> getWarpPosition(Player player, Block warpPad) {
        return Optional.ofNullable(playerWarpPads.get(player.getUUID()))
                .map(warpPads -> warpPads.get(warpPad));
    }

    public static boolean saveWarpPosition(ServerPlayer player, Block warpPad, BlockPos pos) {
        Map<Block, BlockPos> warpPads = playerWarpPads.computeIfAbsent(player.getUUID(), k -> new HashMap<>());
        BlockPos previousPos = warpPads.put(warpPad, pos);
        boolean isNewPosition = previousPos == null || !previousPos.equals(pos);

//        if (previousPos != null && !previousPos.equals(pos)) {
//            player.sendSystemMessage(Component.translatable("superslegend.message.warp_saved").withStyle(ChatFormatting.GREEN));
//        }
        if (isNewPosition) {
            savePlayerData(player);
        }

        return isNewPosition;
    }

    private static void savePlayerData(ServerPlayer player) {
        CompoundTag playerData = player.getPersistentData();
        CompoundTag modData = playerData.getCompound(Player.PERSISTED_NBT_TAG);

        ListTag warpPadsList = new ListTag();
        Map<Block, BlockPos> warpPads = playerWarpPads.get(player.getUUID());
        if (warpPads != null) {
            warpPads.forEach((block, pos) -> {
                CompoundTag warpPadTag = new CompoundTag();
                warpPadTag.putString("Block", ForgeRegistries.BLOCKS.getKey(block).toString());
                warpPadTag.putInt("X", pos.getX());
                warpPadTag.putInt("Y", pos.getY());
                warpPadTag.putInt("Z", pos.getZ());
                warpPadsList.add(warpPadTag);
            });
        }

        modData.put(NBT_KEY, warpPadsList);
        playerData.put(Player.PERSISTED_NBT_TAG, modData);
    }

    private static void loadPlayerData(ServerPlayer player) {
        CompoundTag playerData = player.getPersistentData();
        CompoundTag modData = playerData.getCompound(Player.PERSISTED_NBT_TAG);

        if (modData.contains(NBT_KEY, Tag.TAG_LIST)) {
            ListTag warpPadsList = modData.getList(NBT_KEY, Tag.TAG_COMPOUND);
            Map<Block, BlockPos> warpPads = new HashMap<>();

            for (int i = 0; i < warpPadsList.size(); i++) {
                CompoundTag warpPadTag = warpPadsList.getCompound(i);
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(warpPadTag.getString("Block")));
                BlockPos pos = new BlockPos(warpPadTag.getInt("X"), warpPadTag.getInt("Y"), warpPadTag.getInt("Z"));
                warpPads.put(block, pos);
            }

            playerWarpPads.put(player.getUUID(), warpPads);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            loadPlayerData((ServerPlayer) event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        playerWarpPads.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            loadPlayerData((ServerPlayer) event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        Player original = event.getOriginal();
        Player player = event.getEntity();

        original.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG).copy();
        CompoundTag originData = original.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        CompoundTag newData = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);

        if (originData.contains(NBT_KEY)) {
            newData.put(NBT_KEY, originData.get(NBT_KEY));
            player.getPersistentData().put(Player.PERSISTED_NBT_TAG, newData);
        }
    }
}