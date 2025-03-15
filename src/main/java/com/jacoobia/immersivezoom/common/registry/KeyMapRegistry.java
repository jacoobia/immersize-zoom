package com.jacoobia.immersivezoom.common.registry;

import com.jacoobia.immersivezoom.common.Constants;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

import static com.jacoobia.immersivezoom.common.Translations.Keybindings.CATEGORY;
import static com.jacoobia.immersivezoom.common.Translations.Keybindings.ZOOM;

public class KeyMapRegistry {

    public static final KeyMapping ZOOM_TOGGLE =
            new KeyMapping(
                    ZOOM.getString(),
                    KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYSYM,
                    Constants.DEFAULT_ZOOM_KEY,
                    CATEGORY.getString());

}
