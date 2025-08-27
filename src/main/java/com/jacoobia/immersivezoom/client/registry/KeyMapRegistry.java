package com.jacoobia.immersivezoom.client.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

import static com.jacoobia.immersivezoom.common.Constants.DEFAULT_ZOOM_KEY;
import static com.jacoobia.immersivezoom.common.Translations.Keybindings.CATEGORY;
import static com.jacoobia.immersivezoom.common.Translations.Keybindings.ZOOM;

public class KeyMapRegistry {

    public static final KeyMapping ZOOM_KEYMAPPING =
            new KeyMapping(
                    ZOOM.getString(),
                    KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYSYM,
                    DEFAULT_ZOOM_KEY,
                    CATEGORY.getString());

}
