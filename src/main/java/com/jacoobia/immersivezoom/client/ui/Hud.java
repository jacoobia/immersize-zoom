package com.jacoobia.immersivezoom.client.ui;

import com.jacoobia.immersivezoom.common.Constants;
import com.jacoobia.immersivezoom.client.event.ClientInputSubscriber;
import com.jacoobia.immersivezoom.common.item.AttunedSpyglass;
import com.jacoobia.immersivezoom.common.registry.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class Hud {

    @SubscribeEvent
    public static void onRenderHUD(RenderGuiEvent.Post event) {
        if(Minecraft.getInstance().player == null) return;
        Player player = Minecraft.getInstance().player;
        if(!player.isScoping() || !player.getUseItem().is(ItemRegistry.ATTUNED_SPYGLASS)) return;
        ItemStack stack = player.getUseItem();
        GuiGraphics gui = event.getGuiGraphics();
        gui.drawString(Minecraft.getInstance().font, String.valueOf(AttunedSpyglass.getZoomLevel(stack)), 30, 30, 0xFFFFFF);
    }

}
