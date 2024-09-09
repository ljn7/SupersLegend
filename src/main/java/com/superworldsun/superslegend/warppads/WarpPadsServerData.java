package com.superworldsun.superslegend.warppads;

import com.superworldsun.superslegend.blocks.WarpPadBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class WarpPadsServerData extends SavedData {
    private final Map<BlockPos, WarpPadBlock> warpPads = new HashMap<>();
    private static final String DATA_ID = "warppads";

    private WarpPadsServerData() {
        // Empty constructor
    }

    public static WarpPadsServerData create() {
        return new WarpPadsServerData();
    }

    public static WarpPadsServerData load(CompoundTag nbt) {
        WarpPadsServerData data = new WarpPadsServerData();
        ListTag waypointsListNbt = nbt.getList(DATA_ID, Tag.TAG_COMPOUND);
        waypointsListNbt.forEach(tag -> data.loadWarpPadFromNBT((CompoundTag) tag));
        return data;
    }

    private void loadWarpPadFromNBT(CompoundTag nbt) {
        int posX = nbt.getInt("x");
        int posY = nbt.getInt("y");
        int posZ = nbt.getInt("z");
        BlockPos pos = new BlockPos(posX, posY, posZ);
        ResourceLocation warpPadId = new ResourceLocation(nbt.getString("WarpPadId"));
        WarpPadBlock warpPad = (WarpPadBlock) ForgeRegistries.BLOCKS.getValue(warpPadId);
        addWarpPad(pos, warpPad);
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag warpPadsListNbt = new ListTag();
        warpPads.forEach((pos, warpPad) -> saveWarpPadToNBT(pos, warpPad, warpPadsListNbt));
        nbt.put(DATA_ID, warpPadsListNbt);
        return nbt;
    }

    private void saveWarpPadToNBT(BlockPos pos, WarpPadBlock warpPad, ListTag listNbt) {
        CompoundTag warpPadNBT = new CompoundTag();
        warpPadNBT.putInt("x", pos.getX());
        warpPadNBT.putInt("y", pos.getY());
        warpPadNBT.putInt("z", pos.getZ());
        String warpPadId = ForgeRegistries.BLOCKS.getKey(warpPad).toString();
        warpPadNBT.putString("WarpPadId", warpPadId);
        listNbt.add(warpPadNBT);
    }

    public void placeWarpPad(BlockPos pos, WarpPadBlock warpPad) {
        warpPads.put(pos, warpPad);
        setDirty();
    }

    public WarpPadBlock getWarpPad(BlockPos pos) {
        return warpPads.get(pos);
    }

    private void addWarpPad(BlockPos pos, WarpPadBlock warpPad) {
        warpPads.put(pos, warpPad);
    }

    public void removeWarpPad(BlockPos waypointPos) {
        warpPads.remove(waypointPos);
        setDirty();
    }

    public static WarpPadsServerData instance(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(
                WarpPadsServerData::load,
                WarpPadsServerData::create,
                DATA_ID
        );
    }
}