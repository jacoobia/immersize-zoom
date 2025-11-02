package com.jacoobia.immersivezoom.client.temp;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;


public class HipTunerScreen extends Screen {
    private static final double STEP = 0.1;

    private EditBox xField, yField, zField;

    public HipTunerScreen() { super(Component.literal("Hip Offset Tuner")); }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y0 = this.height / 3;
        int rowH = 24;
        int fieldW = 100;
        int btnW = 22;
        int gap = 6;

        // X row
        xField = numberBox(cx - fieldW/2, y0, fieldW);
        xField.setValue(fmt(HipTuner.x()));
        this.addRenderableWidget(Button.builder(Component.literal("–"), b -> bumpX(-STEP))
                .bounds(xField.getX() - gap - btnW, y0, btnW, 20).build());
        this.addRenderableWidget(xField);
        this.addRenderableWidget(Button.builder(Component.literal("+"), b -> bumpX(+STEP))
                .bounds(xField.getX() + fieldW + gap, y0, btnW, 20).build());

        // Y row
        int y1 = y0 + rowH;
        yField = numberBox(cx - fieldW/2, y1, fieldW);
        yField.setValue(fmt(HipTuner.y()));
        this.addRenderableWidget(Button.builder(Component.literal("–"), b -> bumpY(-STEP))
                .bounds(yField.getX() - gap - btnW, y1, btnW, 20).build());
        this.addRenderableWidget(yField);
        this.addRenderableWidget(Button.builder(Component.literal("+"), b -> bumpY(+STEP))
                .bounds(yField.getX() + fieldW + gap, y1, btnW, 20).build());

        // Z row
        int y2 = y1 + rowH;
        zField = numberBox(cx - fieldW/2, y2, fieldW);
        zField.setValue(fmt(HipTuner.z()));
        this.addRenderableWidget(Button.builder(Component.literal("–"), b -> bumpZ(-STEP))
                .bounds(zField.getX() - gap - btnW, y2, btnW, 20).build());
        this.addRenderableWidget(zField);
        this.addRenderableWidget(Button.builder(Component.literal("+"), b -> bumpZ(+STEP))
                .bounds(zField.getX() + fieldW + gap, y2, btnW, 20).build());

        // Bottom buttons
        int yB = y2 + rowH + 10;
        this.addRenderableWidget(Button.builder(Component.literal("Apply"), b -> applyFromFields())
                .bounds(cx - 150, yB, 90, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("Reset"), b -> resetDefaults())
                .bounds(cx - 45, yB, 90, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("Close"), b -> onClose())
                .bounds(cx + 60, yB, 90, 20).build());
    }

    private EditBox numberBox(int x, int y, int w) {
        EditBox box = new EditBox(this.font, x, y, w, 20, Component.empty());
        box.setMaxLength(32);
        // Only allow -, digits, and a single dot
        box.setFilter(s -> s.isEmpty() || s.matches("-?\\d*(\\.\\d*)?"));
        box.setResponder(s -> {
            // Live-apply while typing (if parseable)
            Double px = parse(box.getValue());
            if (box == xField && px != null) HipTuner.setX(px);
            if (box == yField && px != null) HipTuner.setY(px);
            if (box == zField && px != null) HipTuner.setZ(px);
        });
        return box;
    }

    private void bumpX(double d) { HipTuner.setX(round(HipTuner.x() + d)); xField.setValue(fmt(HipTuner.x())); }
    private void bumpY(double d) { HipTuner.setY(round(HipTuner.y() + d)); yField.setValue(fmt(HipTuner.y())); }
    private void bumpZ(double d) { HipTuner.setZ(round(HipTuner.z() + d)); zField.setValue(fmt(HipTuner.z())); }

    private void applyFromFields() {
        Double px = parse(xField.getValue());
        Double py = parse(yField.getValue());
        Double pz = parse(zField.getValue());
        if (px != null && py != null && pz != null) {
            HipTuner.set(round(px), round(py), round(pz));
            minecraft.player.displayClientMessage(Component.literal(curText()), false);
        }
        // rewrite fields to normalized values
        xField.setValue(fmt(HipTuner.x()));
        yField.setValue(fmt(HipTuner.y()));
        zField.setValue(fmt(HipTuner.z()));
    }

    private void resetDefaults() {
        HipTuner.set(0.18, 0.95, 0.18);
        xField.setValue(fmt(HipTuner.x()));
        yField.setValue(fmt(HipTuner.y()));
        zField.setValue(fmt(HipTuner.z()));
        minecraft.player.displayClientMessage(Component.literal("Hip offsets reset."), false);
    }

    private static String fmt(double d) { return String.format(java.util.Locale.ROOT, "%.3f", d); }
    private static Double parse(String s) {
        if (s == null || s.isEmpty() || s.equals("-") || s.equals(".")) return null;
        try { return Double.parseDouble(s); } catch (NumberFormatException e) { return null; }
    }
    private static double round(double d) { return Math.round(d * 1000.0) / 1000.0; }


    @Override
    public void render(@NotNull GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
        super.render(gg, mouseX, mouseY, partialTick);
//        this.renderBackground(gg, mouseX, mouseY, partialTick);

        // title
        gg.drawCenteredString(this.font, this.title, this.width / 2, this.height / 3 - 18, 0xFFFFFF);

        // field labels
        gg.drawString(this.font, "X", xField.getX() - 12, xField.getY() + 6, 0xAAAAAA, false);
        gg.drawString(this.font, "Y", yField.getX() - 12, yField.getY() + 6, 0xAAAAAA, false);
        gg.drawString(this.font, "Z", zField.getX() - 12, zField.getY() + 6, 0xAAAAAA, false);

        // live preview text
        String cur = String.format(java.util.Locale.ROOT, "x=%.3f y=%.3f z=%.3f",
                HipTuner.x(),
                HipTuner.y(),
                HipTuner.z());
        gg.drawCenteredString(this.font, cur, this.width / 2, zField.getY() + 28, 0x55FF55);
    }

    private static String curText() {
        return String.format(java.util.Locale.ROOT, "x=%.3f y=%.3f z=%.3f", HipTuner.x(), HipTuner.y(), HipTuner.z());
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
