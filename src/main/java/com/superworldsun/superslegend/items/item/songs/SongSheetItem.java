package com.superworldsun.superslegend.items.item.songs;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.superworldsun.superslegend.Config;
import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.songs.LearnedSongs;
import com.superworldsun.superslegend.songs.OcarinaSong;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = SupersLegendMain.MOD_ID)
public abstract class SongSheetItem extends Item {
    private final Supplier<OcarinaSong> songSupplier;
    private static final ResourceLocation texture = new ResourceLocation(SupersLegendMain.MOD_ID, "textures/gui/ocarina.png");
    public SongSheetItem(Supplier<OcarinaSong> songSupplier) {
        super(new Item.Properties().stacksTo(1));
        this.songSupplier = songSupplier;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderTooltip(RenderTooltipEvent.Color event) {
        if (event.getItemStack().getItem() instanceof SongSheetItem songSheet) {
            String notes = songSheet.getSong().getSongPattern();
            Font font = Minecraft.getInstance().font;
            GuiGraphics guiGraphics = event.getGraphics();
            PoseStack poseStack = guiGraphics.pose();

            int noteWidth = 11;
            int notesSpacing = 3;
            int notesIconsWidth = notes.length() * noteWidth + (notes.length() - 1) * notesSpacing;

            List<ClientTooltipComponent> tooltipComponents = event.getComponents();
            int tooltipWidth = 0;
            int tooltipHeight = 0;
            int lastHeight = 0;
            for (ClientTooltipComponent component : tooltipComponents) {
                tooltipWidth = Math.max(tooltipWidth, component.getWidth(font));
                lastHeight = tooltipHeight;
                tooltipHeight += component.getHeight();
            }
            tooltipHeight = tooltipHeight - (tooltipHeight - lastHeight);


            int x = event.getX() + (tooltipWidth - notesIconsWidth) / 2;
            int y = event.getY() + tooltipHeight - 36;

            poseStack.pushPose();

            poseStack.translate(0, 0, 800);



            RenderSystem.setShaderTexture(0, texture);
            guiGraphics.blit(texture, x, y , 0, 20,0, notesIconsWidth + 6, 30, 256, 256);


            // Render notes
            for (int i = 0; i < notes.length(); i++) {
                int noteX = x + (noteWidth + notesSpacing) * i;
                int noteY = y;
                int noteU = 0;

                switch (notes.charAt(i)) {
                    case 'a':
                        noteU = 44;
                        noteY += 18;
                        break;
                    case 'd':
                        noteU = 33;
                        noteY += 14;
                        break;
                    case 'r':
                        noteU = 22;
                        noteY += 10;
                        break;
                    case 'l':
                        noteU = 11;
                        noteY += 6;
                        break;
                    case 'u':
                        noteY += 2;
                        break;
                }

                guiGraphics.blit(texture,
                        noteX, noteY, 0, noteU, 30, noteWidth, noteWidth, 256, 256);

            }

            poseStack.popPose();
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