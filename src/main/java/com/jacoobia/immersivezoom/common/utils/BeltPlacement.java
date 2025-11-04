package com.jacoobia.immersivezoom.common.utils;

import com.jacoobia.immersivezoom.common.Translations;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.TranslatableEnum;
import org.jetbrains.annotations.NotNull;

public enum BeltPlacement implements TranslatableEnum {
    FRONT_LEFT,
    FRONT_RIGHT,
    SIDE_LEFT,
    SIDE_RIGHT,
    BACK_LEFT,
    BACK_RIGHT;

    public boolean isLeft() {
        return this == FRONT_LEFT || this == SIDE_LEFT || this == BACK_LEFT;
    }

    /**
     * @return the display name used by the config screen
     */
    @Override
    public @NotNull Component getTranslatedName() {
        return Translations.Config.beltPlacement(this);
    }
}
