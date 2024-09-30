package com.superworldsun.superslegend.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.capability.waypoint.Waypoint;
import com.superworldsun.superslegend.capability.waypoint.WaypointsProvider;
import com.superworldsun.superslegend.network.NetworkDispatcher;
import com.superworldsun.superslegend.network.message.AttemptTeleportationMessage;
import com.superworldsun.superslegend.network.message.RemoveWaypointMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class WaypointsScreen extends Screen {
    private List<Button> waypointsButtons = new ArrayList<>();
    private List<Waypoint> waypoints = new ArrayList<>();
    private Button removeWaypointButton;
    private Waypoint lastHoveredWaypoint;
    private int buttonsTop;
    private Player player;

    public WaypointsScreen(Player player) {
        super(Component.translatable("screen.waypoints.title"));
        this.player = player;
    }

    @Override
    protected void init() {
        waypoints.clear();
        waypointsButtons.clear();
        List<Waypoint> waypoints = WaypointsProvider.get(player).getWaypoints();
        buttonsTop = height / 2 - 10 - waypoints.size() / 4 * 25;
        int buttonsLeft = width / 2 - 110;

        for (int i = 0; i < waypoints.size(); i++) {
            Waypoint waypoint = waypoints.get(i);
            String buttonText = waypoint.getName();

            if (font.width(buttonText) > 98) {
                while (font.width(buttonText + "...") > 98) {
                    buttonText = buttonText.substring(0, buttonText.length() - 1);
                }
                buttonText += "...";
            }

            Component buttonTextComponent = Component.literal(buttonText);
            Button waypointButton = Button.builder(buttonTextComponent, button -> attemptTeleporting(waypoint))
                    .bounds(buttonsLeft + i % 2 * 120, buttonsTop + i / 2 * 25, 100, 20)
                    .build();
            addRenderableWidget(waypointButton);
            this.waypointsButtons.add(waypointButton);
            this.waypoints.add(waypoint);
        }

        addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), button -> onClose())
                .bounds(width - 110, height - 30, 100, 20)
                .build());

        removeWaypointButton = addRenderableWidget(new RemoveButton(-30, -30, button -> removeWaypoint()));
        addRenderableWidget(removeWaypointButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, getTitle(), width / 2, buttonsTop - 15, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        for (int i = 0; i < waypointsButtons.size(); i++) {
            Button hoveredButton = waypointsButtons.get(i);
            if (hoveredButton.isHovered()) {
                removeWaypointButton.setY(hoveredButton.getY());
                removeWaypointButton.setX(hoveredButton.getX() - 25 + (i % 2) * 130);
                lastHoveredWaypoint = waypoints.get(i);
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void renderRemoveTooltip(Button button, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.renderTooltip(this.font,
                Component.translatable("screen.waypoints.remove_tooltip" + (hasShiftDown() ? "_shift" : "")),
                mouseX, mouseY);
    }

    private void removeWaypoint() {
        if (hasShiftDown()) {
            NetworkDispatcher.network_channel.sendToServer(new RemoveWaypointMessage(lastHoveredWaypoint.getStatuePosition()));
        }
    }
    private void attemptTeleporting(Waypoint waypoint) {
        NetworkDispatcher.network_channel.sendToServer(new AttemptTeleportationMessage(waypoint.getStatuePosition()));
        onClose();
    }

    public static class RemoveButton extends Button {
        public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation(SupersLegendMain.MOD_ID, "textures/gui/widgets.png");

        public RemoveButton(int x, int y, OnPress onPress) {
            super(Button.builder(Component.empty(), onPress)
                    .pos(x, y)
                    .size(20, 20));
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();

            int yOffset = (isHovered() && Screen.hasShiftDown() ? 1 : 0) * 20;
            guiGraphics.blit(WIDGETS_LOCATION, getX(), getY(), 0, yOffset, width, height);
        }
    }
}