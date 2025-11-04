package com.jacoobia.immersivezoom.client.event;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;

import static com.jacoobia.immersivezoom.common.Constants.*;

/**
 * Draws the spyglass slot's frame in the players inventory
 * TODO: Drive this from config settings for the slot position for modpacks
 */
@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientScreenSubscriber {

    private static final ResourceLocation CREATIVE_INVENTORY_TAB =
            ResourceLocation.withDefaultNamespace("textures/gui/container/creative_inventory/tab_inventory.png");

    private static final int SLOT_FRAME_SIZE = 18;

    @SubscribeEvent
    public static void onContainerBackground(ContainerScreenEvent.Render.Background event) {
        AbstractContainerScreen<?> screen = event.getContainerScreen();
        GuiGraphics graphics = event.getGuiGraphics();
        int left = screen.getGuiLeft();
        int top = screen.getGuiTop();

        // The frame sits 1px up and left of the slot's item position
        if (screen instanceof CreativeModeInventoryScreen creative) {
            if (!creative.isInventoryOpen()) return;
            graphics.blit(CREATIVE_INVENTORY_TAB,
                    left + CREATIVE_SPYGLASS_SLOT_X - 1, top + CREATIVE_SPYGLASS_SLOT_Y - 1,
                    34, 19, SLOT_FRAME_SIZE, SLOT_FRAME_SIZE);
        } else if (screen instanceof InventoryScreen) {
            graphics.blit(AbstractContainerScreen.INVENTORY_LOCATION,
                    left + SPYGLASS_SLOT_X - 1, top + SPYGLASS_SLOT_Y - 1,
                    76, 61, SLOT_FRAME_SIZE, SLOT_FRAME_SIZE);
        }
    }

}
