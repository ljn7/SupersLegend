package com.superworldsun.superslegend.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.superworldsun.superslegend.network.NetworkDispatcher;
import com.superworldsun.superslegend.network.message.SetGossipStoneTextMessage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class GossipStoneScreen extends Screen {
    private final BlockPos gossipStonePos;
    private EditBox textField;

    public GossipStoneScreen(BlockPos pos) {
        super(Component.empty());
        gossipStonePos = pos;
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose())
                .bounds(this.width / 2 - 100, this.height / 4 + 120, 200, 20)
                .build());

        textField = new EditBox(this.font, this.width / 2 - 100, this.height / 2 - 10, 200, 20, Component.empty());
        addRenderableWidget(textField);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void tick() {
        textField.tick();
    }

    @Override
    public void onClose() {
        NetworkDispatcher.network_channel.sendToServer(new SetGossipStoneTextMessage(gossipStonePos, textField.getValue()));
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}