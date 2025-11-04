package com.jacoobia.immersivezoom.client.config;

import com.jacoobia.immersivezoom.common.Translations;
import com.jacoobia.immersivezoom.common.utils.BeltPlacement;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.EnumValue<BeltPlacement> BELT_PLACEMENT = BUILDER
            .comment(Translations.Config.BELT_PLACEMENT_TOOLTIP)
            .translation(Translations.Config.BELT_PLACEMENT)
            .defineEnum("beltPlacement", BeltPlacement.BACK_RIGHT);

    public static final ModConfigSpec.BooleanValue SHOW_BELT_SPYGLASS = BUILDER
            .comment(Translations.Config.SHOW_BELT_SPYGLASS_TOOLTIP)
            .translation(Translations.Config.SHOW_BELT_SPYGLASS)
            .define("showBeltSpyglass", true);

    public static final ModConfigSpec.BooleanValue RENDER_BELT_SPYGLASSES = BUILDER
            .comment(Translations.Config.RENDER_BELT_SPYGLASSES_TOOLTIP)
            .translation(Translations.Config.RENDER_BELT_SPYGLASSES)
            .define("renderBeltSpyglasses", true);

    public static final ModConfigSpec.BooleanValue BELT_SWAY = BUILDER
            .comment(Translations.Config.BELT_SWAY_TOOLTIP)
            .translation(Translations.Config.BELT_SWAY)
            .define("beltSway", true);

    public static final ModConfigSpec.BooleanValue ZOOM_SOUND = BUILDER
            .comment(Translations.Config.ZOOM_SOUND_TOOLTIP)
            .translation(Translations.Config.ZOOM_SOUND)
            .define("zoomSound", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

}
