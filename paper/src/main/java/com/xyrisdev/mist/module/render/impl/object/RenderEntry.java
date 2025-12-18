package com.xyrisdev.mist.module.render.impl.object;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public record RenderEntry(
		@NotNull UUID id,
		@NotNull Player owner,
		@NotNull RenderType type,
		@NotNull List<ItemStack> items
) {}