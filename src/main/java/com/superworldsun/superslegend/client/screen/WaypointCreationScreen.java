package com.superworldsun.superslegend.client.screen;

import com.superworldsun.superslegend.network.NetworkDispatcher;
import com.superworldsun.superslegend.network.message.SetWaypointNameMessage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WaypointCreationScreen extends Screen {
    private final BlockPos waypointPos;
    private EditBox nameTextField;
    private Button doneButton;
    private Player player;
    private Vec3 teleportPos;
    private final Direction facing;

    public WaypointCreationScreen(BlockPos pos, Vec3 teleportPos, Direction facing, Player player) {
        super(Component.translatable("screen.waypoints.title"));
        waypointPos = pos;
        this.player = player;
        this.facing = facing;
        this.teleportPos = teleportPos;
    }

    @Override
    protected void init() {
        nameTextField = new EditBox(this.font, this.width / 2 - 100, this.height / 2 - 10, 200, 20, Component.empty());
        this.addRenderableWidget(nameTextField);

        doneButton = Button.builder(Component.translatable("gui.done"), (button) -> this.onClose())
                .bounds(this.width / 2 - 50, this.height / 2 + 20, 100, 20)
                .build();
        this.addRenderableWidget(doneButton);

        doneButton.active = false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawString(this.font, Component.translatable("screen.waypoints.enter_name"), this.width / 2 - 100, this.height / 2 - 25, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        nameTextField.tick();
        doneButton.active = !nameTextField.getValue().isEmpty();
    }

    @Override
    public void onClose() {
        if (!nameTextField.getValue().isEmpty()) {
            NetworkDispatcher.network_channel.sendToServer(new SetWaypointNameMessage(waypointPos, teleportPos, facing, nameTextField.getValue(), player.getUUID()));
        }

        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}