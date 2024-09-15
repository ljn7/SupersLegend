package com.superworldsun.superslegend.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.superworldsun.superslegend.Config;
import com.superworldsun.superslegend.SupersLegendMain;
import com.superworldsun.superslegend.client.config.SupersLegendConfig;
import com.superworldsun.superslegend.events.TemperatureEvents;
import com.superworldsun.superslegend.registries.AttributeInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.opengl.NVVertexBufferUnifiedMemory;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = SupersLegendMain.MOD_ID, value = Dist.CLIENT)
public class ThermometerHud {
    private static final ResourceLocation TERMOMETER_TEXTURE = new ResourceLocation(SupersLegendMain.MOD_ID, "textures/gui/termometer.png");
    private static float prev_arrow_rotation;
    private static float arrow_rotation;
    private static float prev_heat_level;
    private static float heat_level = 0.31F;
    private static float prev_cold_level;
    private static float cold_level = 0.31F;

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        if (!Config.isTemperatureEnabled())
            return;

        // Render thermometer right after food
        if (event.getOverlay() == VanillaGuiOverlay.FOOD_LEVEL.type()) {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;

            if (player == null) {
                return;
            }

            GuiGraphics guiGraphics = event.getGuiGraphics();
            float temperature = TemperatureEvents.getTemperature(player);
            int termometerSizeX = 33;
            int termometerSizeY = 33;
            int termometerX = 2;
            int termometerY = event.getWindow().getGuiScaledHeight() - 2 - termometerSizeY;

            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, TERMOMETER_TEXTURE);

            renderBackground(guiGraphics, termometerX, termometerY, termometerSizeX, termometerSizeY);
            renderDangerousColdLevel(guiGraphics, termometerX, termometerY, termometerSizeX, termometerSizeY, event.getPartialTick());
            renderDangerousHeatLevel(guiGraphics, termometerX, termometerY, termometerSizeX, termometerSizeY, event.getPartialTick());
            renderOverlay(guiGraphics, termometerX, termometerY, termometerSizeX, termometerSizeY);
            renderArrow(guiGraphics, termometerX, termometerY, termometerSizeX, termometerSizeY, event.getPartialTick(), temperature);
            renderDebugInfo(guiGraphics, termometerX, termometerY, termometerSizeX, termometerSizeY, event.getPartialTick(), temperature);

            // Switch texture back to vanilla one
            RenderSystem.setShaderTexture(0, new ResourceLocation("textures/gui/icons.png"));
            RenderSystem.disableBlend();
        }
    }

    private static void renderBackground(GuiGraphics guiGraphics, int termometerX, int termometerY, int termometerSizeX, int termometerSizeY) {
        guiGraphics.blit(TERMOMETER_TEXTURE, termometerX, termometerY, 0, 0, termometerSizeX, termometerSizeY);
    }

    private static void renderDangerousColdLevel(GuiGraphics guiGraphics, int termometerX, int termometerY, int termometerSizeX, int termometerSizeY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        double coldResistance = player.getAttributeValue(AttributeInit.COLD_RESISTANCE.get()) - 1;
        float targetColdLevel = 0.31F;

        if (coldResistance < 0.0D) {
            targetColdLevel += 0.19F * Math.abs(coldResistance);
        } else if (coldResistance > 0.0D) {
            targetColdLevel -= targetColdLevel * coldResistance;
        }

        if (cold_level < targetColdLevel) {
            cold_level += 0.01F;
        } else if (cold_level > targetColdLevel) {
            cold_level -= 0.01F;
        }

        if (Math.abs(cold_level - targetColdLevel) < 0.01F) {
            cold_level = targetColdLevel;
        }

        float coldLevelAnimation = Mth.lerp(partialTicks, prev_cold_level, cold_level);
        blitCircular(guiGraphics, termometerX, termometerY, 33, 0, termometerSizeX, termometerSizeY, false, coldLevelAnimation);
        prev_cold_level = cold_level;
    }

    private static void renderDangerousHeatLevel(GuiGraphics guiGraphics, int termometerX, int termometerY, int termometerSizeX, int termometerSizeY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        double heatResistance = TemperatureEvents.getHeatResistance(player);

        float targetHeatLevel = 0.31F;

        if (heatResistance < 0.0D) {
            targetHeatLevel += 0.19F * Math.abs(heatResistance);
        } else if (heatResistance > 0.0D) {
            targetHeatLevel -= targetHeatLevel * heatResistance;
        }

        if (heat_level < targetHeatLevel) {
            heat_level += 0.01F;
        } else if (heat_level > targetHeatLevel) {
            heat_level -= 0.01F;
        }

        if (Math.abs(heat_level - targetHeatLevel) < 0.01F) {
            heat_level = targetHeatLevel;
        }

        float heatLevelAnimation = Mth.lerp(partialTicks, prev_heat_level, heat_level);
        blitCircular(guiGraphics, termometerX, termometerY, 66, 0, termometerSizeX, termometerSizeY, true, heatLevelAnimation);
        prev_heat_level = heat_level;
    }

    private static void renderOverlay(GuiGraphics guiGraphics, int termometerX, int termometerY, int termometerSizeX, int termometerSizeY) {
        guiGraphics.blit(TERMOMETER_TEXTURE, termometerX, termometerY, 99, 0, termometerSizeX, termometerSizeY);
    }

    private static void renderArrow(GuiGraphics guiGraphics, int termometerX, int termometerY, int termometerSizeX, int termometerSizeY, float partialTicks, float temperature) {
        float maxArrowRotationNormal = 50F;
        float maxArrowRotationCritical = 125F;
        float arrowShift = -50F;
        float targetArrowRotation;
        float arrowSpeed = 0.2F;

        if (temperature >= 0 && temperature <= 1) {
            targetArrowRotation = temperature * (maxArrowRotationNormal - arrowShift) + arrowShift;
        } else if (temperature < 0) {
            targetArrowRotation = arrowShift + temperature * (maxArrowRotationCritical - maxArrowRotationNormal);
        } else {
            targetArrowRotation = maxArrowRotationNormal + (temperature - 1) * (maxArrowRotationCritical - maxArrowRotationNormal);
        }

        targetArrowRotation = Mth.clamp(targetArrowRotation, -maxArrowRotationCritical, maxArrowRotationCritical);

        if (arrow_rotation < targetArrowRotation) {
            arrow_rotation += arrowSpeed;
        } else if (arrow_rotation > targetArrowRotation) {
            arrow_rotation -= arrowSpeed;
        }

        if (Math.abs(arrow_rotation - targetArrowRotation) < arrowSpeed) {
            arrow_rotation = targetArrowRotation;
        }

        float arrowRotationAnimation = Mth.lerp(partialTicks, prev_arrow_rotation, arrow_rotation);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(termometerX + termometerSizeX / 2f, termometerY + termometerSizeY / 2f, 0);
        guiGraphics.pose().mulPose(new Quaternionf().rotationZ(arrowRotationAnimation * ((float) Math.PI / 180F)));
        guiGraphics.pose().translate(-termometerX - termometerSizeX / 2f, -termometerY - termometerSizeY / 2f, 0);
        guiGraphics.blit(TERMOMETER_TEXTURE, termometerX, termometerY, 132, 0, termometerSizeX, termometerSizeY);
        guiGraphics.pose().popPose();
        prev_arrow_rotation = arrow_rotation;
    }

    private static void renderDebugInfo(GuiGraphics guiGraphics, int termometerX, int termometerY, int termometerSizeX, int termometerSizeY, float partialTicks, float temperature) {
        // Uncomment and modify as needed for debug information
        // String formattedTemperature = String.format("%.2f", temperature);
        // guiGraphics.drawCenteredString(Minecraft.getInstance().font, formattedTemperature, termometerX + termometerSizeX / 2, termometerY - 10, 0xFFFFFF);
    }

    private static void blitCircular(GuiGraphics guiGraphics, float x, float y, float u, float v, float xSize, float ySize, boolean reverse, float fillPercentage) {
        if (fillPercentage <= 0) {
            return;
        }

        Matrix4f pose = guiGraphics.pose().last().pose();
        float uSize = xSize / 256f;
        float vSize = ySize / 256f;
        u /= 256f;
        v /= 256f;

        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        bufferbuilder.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_TEX);

        if (reverse) {
            float firstThirdFill = fillPercentage > 1 / 3F ? 1F : fillPercentage * 3;
            bufferbuilder.vertex(pose, x + xSize, y + ySize, 0).uv(u + uSize, v + vSize).endVertex();
            bufferbuilder.vertex(pose, x + xSize, y + ySize - ySize * firstThirdFill, 0).uv(u + uSize, v + vSize - vSize * firstThirdFill).endVertex();
            bufferbuilder.vertex(pose, x + xSize / 2, y + ySize / 2, 0).uv(u + uSize / 2, v + vSize / 2).endVertex();

            if (fillPercentage > 1 / 3F) {
                float secondThirdFill = fillPercentage > 2 / 3F ? 1F : (fillPercentage - 1 / 3F) * 3;
                bufferbuilder.vertex(pose, x + xSize, y, 0).uv(u + uSize, v).endVertex();
                bufferbuilder.vertex(pose, x + xSize - xSize * secondThirdFill, y, 0).uv(u + uSize - uSize * secondThirdFill, v).endVertex();
                bufferbuilder.vertex(pose, x + xSize / 2, y + ySize / 2, 0).uv(u + uSize / 2, v + vSize / 2).endVertex();
            }

            if (fillPercentage > 2 / 3F) {
                float thirdThirdFill = fillPercentage >= 1F ? 1F : (fillPercentage - 2 / 3F) * 3;
                bufferbuilder.vertex(pose, x, y + ySize * thirdThirdFill, 0).uv(u, v + vSize * thirdThirdFill).endVertex();
                bufferbuilder.vertex(pose, x, y + ySize, 0).uv(u, v + vSize).endVertex();
                bufferbuilder.vertex(pose, x + xSize / 2, y + ySize / 2, 0).uv(u + uSize / 2, v + vSize / 2).endVertex();
            }
        } else {
            float firstThirdFill = fillPercentage > 1 / 3F ? 1F : fillPercentage * 3;
            bufferbuilder.vertex(pose, x, y + ySize - ySize * firstThirdFill, 0).uv(u, v + vSize - vSize * firstThirdFill).endVertex();
            bufferbuilder.vertex(pose, x, y + ySize, 0).uv(u, v + vSize).endVertex();
            bufferbuilder.vertex(pose, x + xSize / 2, y + ySize / 2, 0).uv(u + uSize / 2, v + vSize / 2).endVertex();

            if (fillPercentage > 1 / 3F) {
                float secondThirdFill = fillPercentage > 2 / 3F ? 1F : (fillPercentage - 1 / 3F) * 3;
                bufferbuilder.vertex(pose, x + xSize * secondThirdFill, y, 0).uv(u + uSize * secondThirdFill, v).endVertex();
                bufferbuilder.vertex(pose, x, y, 0).uv(u, v).endVertex();
                bufferbuilder.vertex(pose, x + xSize / 2, y + ySize / 2, 0).uv(u + uSize / 2, v + vSize / 2).endVertex();
            }
        }
        BufferUploader.drawWithShader(bufferbuilder.end());
    }
}
