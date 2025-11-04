package com.jacoobia.immersivezoom.common;

import net.minecraft.network.chat.Component;

import java.util.Locale;

public class Translations {

    public static class Keybindings {
        // Plain keys, not Components: KeyMapping translates these itself, and saves them in options.txt
        public static final String CATEGORY = "key.immersive_zoom.keybinds.category";
        public static final String ZOOM = "key.immersive_zoom.keybinds.keybind.zoom";
    }

    public static class Tooltips {
        public static Component zoomLevel(float zoom) {
            return Component.translatable("tooltip.immersive_zoom.zoom_level", String.format(Locale.ROOT, "%.1f", zoom));
        }
    }

    public static class Lore {
        public static Component attuneLore = Component.translatable("lore.immersive_zoom.attune");
        public static Component midnight = Component.translatable("lore.immersive_zoom.midnight");
        public static Component mined = Component.translatable("lore.immersive_zoom.mined");
    }

    public static class Advancements {
        public static Component rootName = Component.translatable("advancements.immersive_zoom.root.title");
        public static Component rootDescription = Component.translatable("advancements.immersive_zoom.root.description");

        public static Component unattunedAmethyst = Component.translatable("advancements.immersive_zoom.unattuned_amethyst.title");
        public static Component unattunedAmethystDescription = Component.translatable("advancements.immersive_zoom.unattuned_amethyst.description");

        public static Component attunedAmethyst = Component.translatable("advancements.immersive_zoom.attuned_amethyst.title");
        public static Component attunedAmethystDescription = Component.translatable("advancements.immersive_zoom.attuned_amethyst.description");

        public static Component copperFocus = Component.translatable("advancements.immersive_zoom.copper_focus.title");
        public static Component copperFocusDescription = Component.translatable("advancements.immersive_zoom.copper_focus.description");

        public static Component attunedSpyglassName = Component.translatable("advancements.immersive_zoom.attuned_spyglass.title");
        public static Component attunedSpyglassDescription = Component.translatable("advancements.immersive_zoom.attuned_spyglass.description");
    }

}
