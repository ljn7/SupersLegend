package com.superworldsun.superslegend.items.item.songs;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.superworldsun.superslegend.Config;
import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.songs.LearnedSongs;
import com.superworldsun.superslegend.songs.OcarinaSong;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID)
public abstract class SongSheetItem extends Item {
    private final Supplier<OcarinaSong> songSupplier;

    public SongSheetItem(Supplier<OcarinaSong> songSupplier) {
        super(new Item.Properties().stacksTo(1));
        this.songSupplier = songSupplier;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onPreRenderTooltip(RenderTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof SongSheetItem) {
            SongSheetItem songSheet = (SongSheetItem) event.getItemStack().getItem();
            String notes = songSheet.getSong().getSongPattern();
            ResourceLocation texture = new ResourceLocation(SupersLegendMain.MOD_ID, "textures/gui/ocarina.png");

            GuiGraphics guiGraphics = event.getGraphics();
            PoseStack poseStack = guiGraphics.pose();

            int noteWidth = 11;
            int notesSpacing = 3;
            int notesIconsWidth = notes.length() * noteWidth + (notes.length() - 1) * notesSpacing;

            int tooltipWidth = event.getX();
            int tooltipHeight = event.getY();

            int x = event.getX() + (tooltipWidth - notesIconsWidth) / 2;
            int y = event.getY() + tooltipHeight;

            RenderSystem.setShaderTexture(0, texture);
            guiGraphics.blit(texture, x - 3, y - 10, 0, 20, notesIconsWidth + 6, 30, 256, 256);

            for (int i = 0; i < notes.length(); i++) {
                int noteX = x + (noteWidth + notesSpacing) * i;
                int noteY = y;
                int noteU = 0;

                switch (notes.charAt(i)) {
                    case 'a':
                        noteU = 44;
                        noteY += 22;
                        break;
                    case 'd':
                        noteU = 33;
                        noteY += 18;
                        break;
                    case 'r':
                        noteU = 22;
                        noteY += 14;
                        break;
                    case 'l':
                        noteU = 11;
                        noteY += 7;
                        break;
                    case 'u':
                        noteY += 4;
                        break;
                }

                guiGraphics.blit(texture, noteX, noteY, noteU, 30, noteWidth, noteWidth, 256, 256);
            }
        }
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        addSongDescription(list);
        list.add(Component.empty());
        list.add(Component.empty());
        list.add(Component.empty());
        list.add(Component.empty());
        list.add(Component.literal("Right click to Learn Song").withStyle(style -> style.withColor(0xFFD700)));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            LearnedSongs learnedSongs = LearnedSongs.Provider.getLearnedSongs(player);
            Set<OcarinaSong> learnedSongsSet = learnedSongs.getLearnedSongs();

            if (!learnedSongsSet.contains(songSupplier.get())) {
                if (Config.song_sheet_consumed) {
                    player.getItemInHand(hand).shrink(1);
                }

                learnedSongsSet.add(songSupplier.get());
                LearnedSongs.Provider.saveLearnedSongs(player, learnedSongs);
                LearnedSongs.Provider.sync((ServerPlayer) player);
                player.sendSystemMessage(Component.translatable("item.superslegend.song_sheet.learned", songSupplier.get().getLocalizedName()));
                return InteractionResultHolder.success(player.getItemInHand(hand));
            } else {
                player.sendSystemMessage(Component.translatable("item.superslegend.song_sheet.already_know"));
            }
        }

        return InteractionResultHolder.fail(player.getItemInHand(hand));
    }

    public OcarinaSong getSong() {
        return songSupplier.get();
    }

    protected abstract void addSongDescription(List<Component> list);
}