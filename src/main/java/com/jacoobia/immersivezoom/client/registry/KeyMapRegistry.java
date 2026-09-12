package com.jacoobia.immersivezoom.client.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

import static com.jacoobia.immersivezoom.common.Constants.DEFAULT_QUICK_ZOOM_KEY;
import static com.jacoobia.immersivezoom.common.Constants.DEFAULT_ZOOM_KEY;
import static com.jacoobia.immersivezoom.common.Translations.Keybindings.CATEGORY;
import static com.jacoobia.immersivezoom.common.Translations.Keybindings.QUICK_ZOOM;
import static com.jacoobia.immersivezoom.common.Translations.Keybindings.ZOOM;

public class KeyMapRegistry {

    public static final KeyMapping ZOOM_KEYMAPPING =
            new KeyMapping(
                    ZOOM,
                    KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYSYM,
                    DEFAULT_ZOOM_KEY,
                    CATEGORY);

    public static final KeyMapping QUICK_ZOOM_KEYMAPPING =
            new KeyMapping(
                    QUICK_ZOOM,
                    KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYSYM,
                    DEFAULT_QUICK_ZOOM_KEY,
                    CATEGORY);

}
