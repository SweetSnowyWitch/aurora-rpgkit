package com.github.sweetsnowywitch.csmprpgkit.client.overlays;

import com.github.sweetsnowywitch.csmprpgkit.RPGKitMod;
import com.github.sweetsnowywitch.csmprpgkit.client.ClientSpellCastController;
import com.github.sweetsnowywitch.csmprpgkit.components.ActiveCastComponent;
import com.github.sweetsnowywitch.csmprpgkit.components.ModComponents;
import com.github.sweetsnowywitch.csmprpgkit.magic.Aspect;
import com.github.sweetsnowywitch.csmprpgkit.magic.ItemElement;
import com.github.sweetsnowywitch.csmprpgkit.magic.SpellElement;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ActiveCastOverlay implements HudRenderCallback {
    private static final Identifier FRAME_TEXTURE = new Identifier(RPGKitMod.MOD_ID, "textures/hud/frame.png");
    private static final Identifier CHANNEL_BAR_TEXTURE = new Identifier(RPGKitMod.MOD_ID, "textures/hud/channel_bar.png");
    private static final int ELEMENT_SLOT_SIZE = 22;
    public ClientSpellCastController handler;

    public ActiveCastOverlay(ClientSpellCastController handler) {
        this.handler = handler;
    }

    public void renderBuilder(ActiveCastComponent comp, MatrixStack matrix, float tickDelta) {
        var client = MinecraftClient.getInstance();

        var width = client.getWindow().getScaledWidth();
        var height = client.getWindow().getScaledHeight();

        var guiStartHeight = height - 90;
        if (client.player != null && client.player.isCreative()) {
            guiStartHeight += 20;
        }

        // Pending elements.
        var maxElements = comp.getMaxElements();
        var elementGap = (180 - ELEMENT_SLOT_SIZE*maxElements)/(maxElements - 1);

        var drawnAspects = 0;
        var pending = comp.getPendingElements();
        for (int i = 0; i < maxElements; i++) {
            var x = width / 2 - 90 + (22 + elementGap) * drawnAspects;
            var y = guiStartHeight - 5;

            SpellElement element = null;
            if (i < pending.size()) {
                element = pending.get(i);
            }

            this.drawElement(matrix, x, y, element, 1);

            drawnAspects++;
        }

        // Available aspects.
        var elements = comp.getAvailableElements();
        drawnAspects = 0;
        for (var element : elements) {
            var x = width/2 - elements.size()*18/2 + drawnAspects*18;
            var y = guiStartHeight + ELEMENT_SLOT_SIZE;

            this.drawElement(matrix, x, y, element, 0.75f);

            drawnAspects++;
        }
    }

    public void renderChannelBar(ActiveCastComponent comp, MatrixStack matrix, float tickDelta) {
        var client = MinecraftClient.getInstance();

        var age = comp.getChannelAge();
        var maxAge = comp.getChannelMaxAge();
        float factor = (float)(maxAge - age) / maxAge;

        var width = client.getWindow().getScaledWidth();
        var height = client.getWindow().getScaledHeight();
        var guiStartHeight = height - 29;

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, CHANNEL_BAR_TEXTURE);
        DrawableHelper.drawTexture(matrix, width/2 - 91, guiStartHeight, 0, 0,
                182, 5, 182, 10);
        DrawableHelper.drawTexture(matrix, width/2 - 91, guiStartHeight, 0, 5,
                (int)(182*factor), 5, 182, 10);
    }

    @Override
    public void onHudRender(MatrixStack matrix, float tickDelta) {
        var client = MinecraftClient.getInstance();
        if (client.player == null) {
            return;
        }
        var comp = client.player.getComponent(ModComponents.CAST);

        if (comp.isBuilding()) {
            this.renderBuilder(comp, matrix, tickDelta);
        }

        if (comp.isChanneling()) {
            this.renderChannelBar(comp, matrix, tickDelta);
        }
    }

    public void drawElement(MatrixStack matrixStack, int x, int y, @Nullable SpellElement element, float scale) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        RenderSystem.setShaderTexture(0, FRAME_TEXTURE);
        DrawableHelper.drawTexture(matrixStack, x, y, 0, 0,
                (int)(ELEMENT_SLOT_SIZE*scale), (int)(ELEMENT_SLOT_SIZE*scale),
                (int)(ELEMENT_SLOT_SIZE*scale), (int)(ELEMENT_SLOT_SIZE*scale));
        var frameOffset = (ELEMENT_SLOT_SIZE - 16)/2;

        if (element == null) {
            return;
        }

        if (element instanceof Aspect asp) {
            RenderSystem.setShaderTexture(0, asp.getTexturePath());
            DrawableHelper.drawTexture(matrixStack, x+(int)(frameOffset*scale), y+(int)(frameOffset*scale), 0, 0,
                    (int)(16*scale), (int)(16*scale), (int)(16*scale), (int)(16*scale));
            return;
        }

        if (scale < 1f) {
            frameOffset = 0; // TODO: Figure out how to scale items.
        }

        if (element instanceof ItemElement.Stack ies) {
            var renderer = MinecraftClient.getInstance().getItemRenderer();
            renderer.renderInGui(ies.getStack(), x+(int)(frameOffset*scale), y+(int)(frameOffset*scale));
        } else if (element instanceof ItemElement ie) {
            var renderer = MinecraftClient.getInstance().getItemRenderer();
            renderer.renderInGui(new ItemStack(ie.getItem()), x+(int)(frameOffset*scale), y+(int)(frameOffset*scale));
        }
    }
}