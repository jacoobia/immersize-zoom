package com.jacoobia.immersivezoom.common.data;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record AttunedZoomBarTooltip(float progress, float zoom) implements TooltipComponent {
}
