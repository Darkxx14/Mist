package com.xyrisdev.mist.extension.render.inventory.impl.object;

import com.xyrisdev.mist.extension.render.inventory.impl.InventoryRenderService;
import lombok.Builder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Builder
public class InventoryRenderRequest {

	@NotNull
	private final Player owner;

	@NotNull
	private final InventoryRenderType type;

	@NotNull
	private final List<ItemStack> items;

	public @NotNull UUID capture() {
		final UUID id = UUID.randomUUID();

		final List<ItemStack> filteredItems = items.stream()
				.map(item -> item != null ? item : new ItemStack(org.bukkit.Material.AIR))
				.toList();

		final InventoryRenderEntry entry = new InventoryRenderEntry(
				id,
				owner,
				type,
				filteredItems
		);

		return InventoryRenderService.get().capture(entry);
	}
}