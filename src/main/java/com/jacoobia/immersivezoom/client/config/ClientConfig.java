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

    public static final ModConfigSpec SPEC = BUILDER.build();

}
