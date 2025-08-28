package com.jacoobia.immersivezoom.client.ui.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record AttunedZoomBarTooltip(float progress, float zoom) implements TooltipComponent {
}
