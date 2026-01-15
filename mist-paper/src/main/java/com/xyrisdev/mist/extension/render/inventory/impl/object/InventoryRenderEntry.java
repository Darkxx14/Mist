package com.xyrisdev.mist.extension.render.inventory.impl.object;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public record InventoryRenderEntry(
		@NotNull UUID id,
		@NotNull Player owner,
		@NotNull InventoryRenderType type,
		@NotNull List<ItemStack> items
) {}