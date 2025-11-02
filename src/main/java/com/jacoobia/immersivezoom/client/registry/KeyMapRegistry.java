package com.jacoobia.immersivezoom.client.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

import static com.jacoobia.immersivezoom.common.Constants.DEFAULT_ZOOM_KEY;
import static com.jacoobia.immersivezoom.common.Translations.Keybindings.CATEGORY;
import static com.jacoobia.immersivezoom.common.Translations.Keybindings.TOGGLE_TEMP_SCREEN;
import static com.jacoobia.immersivezoom.common.Translations.Keybindings.ZOOM;

public class KeyMapRegistry {

    public static final KeyMapping ZOOM_KEYMAPPING =
            new KeyMapping(
                    ZOOM,
                    KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYSYM,
                    DEFAULT_ZOOM_KEY,
                    CATEGORY);

    public static final KeyMapping TOGGLE_TEMP_SCREEN_KEYMAPPING =
            new KeyMapping(
                    TOGGLE_TEMP_SCREEN,
                    KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYSYM,
                    InputConstants.KEY_P,
                    CATEGORY);

}
