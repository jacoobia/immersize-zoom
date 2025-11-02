package com.jacoobia.immersivezoom.client.utils;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

public record ScopedZoomContext(LocalPlayer player, ItemStack active, boolean vanillaInHand) {

}
