package com.jacoobia.immersivezoom;

import com.jacoobia.immersivezoom.client.config.ClientConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import static com.jacoobia.immersivezoom.common.Constants.MOD_ID;

/**
 * Client only mod entry point so config ONLY loads on the client
 */
@Mod(value = MOD_ID, dist = Dist.CLIENT)
public class ImmersiveZoomClient {

    public ImmersiveZoomClient(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

}
