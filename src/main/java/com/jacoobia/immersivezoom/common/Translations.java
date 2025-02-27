package com.jacoobia.immersivezoom.common;

import net.minecraft.network.chat.Component;

public class Translations {

    public static class Keybindings {
        public static final Component CATEGORY = Component.translatable("key.immersive_zoom.keybinds.category");
        public static final Component ZOOM = Component.translatable("key.immersive_zoom.keybinds.keybind.zoom");
    }

    public static class Tooltips {
        public static Component zoomLevel(float zoomLevel) {
            return Component.translatable("tooltip.immersive_zoom.zoom_level", zoomLevel);
        }
    }
}
