package com.jacoobia.immersivezoom.client.ui.tooltip;

import com.jacoobia.immersivezoom.common.Translations;
import com.jacoobia.immersivezoom.common.data.AttunedZoomBarTooltip;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public record AttunedZoomBarClient(AttunedZoomBarTooltip data) implements ClientTooltipComponent {

    private static final int WIDTH = 90;
    private static final int HEIGHT = 8;
    private static final int PAD = 1;
    private static final int LABEL_GAP = 4;
    private static final int LINE_HEIGHT = 10;

    private static final int COLOR_BORDER = 0xFF000000;
    private static final int COLOR_BG = 0xFF2E2E2E;
    private static final int COLOR_FILL = 0xFF25C14D;

    @Override
    public int getHeight() {
        return LINE_HEIGHT;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return WIDTH + LABEL_GAP + font.width(label());
    }

    private Component label() {
        return Translations.Tooltips.zoomLevel(data.zoom());
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, GuiGraphics g) {
        int w = WIDTH, h = HEIGHT;

        g.fill(x, y, x + w, y + 1, COLOR_BORDER);
        g.fill(x, y + h - 1, x + w, y + h, COLOR_BORDER);
        g.fill(x, y + 1, x + 1, y + h - 1, COLOR_BORDER);
        g.fill(x + w - 1, y + 1, x + w, y + h - 1, COLOR_BORDER);

        g.fill(x + 1, y + 1, x + w - 1, y + h - 1, COLOR_BG);

        float t = Mth.clamp(data.progress(), 0f, 1f);
        int innerW = w - 2 * PAD - 2;
        int fillW = Math.max(0, Math.round(innerW * t));
        if (fillW > 0) {
            g.fill(x + PAD + 1, y + PAD + 1, x + PAD + 1 + fillW, y + h - PAD - 1, COLOR_FILL);
        }

        g.drawString(font, label(), x + w + LABEL_GAP, y, 0xFFFFFFFF);
    }
}
