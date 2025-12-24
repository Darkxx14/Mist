package com.xyrisdev.mist.extension.render.impl.object;

import com.xyrisdev.mist.extension.render.impl.RenderService;
import lombok.Builder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Builder
public class RenderRequest {

	@NotNull
	private final Player owner;

	@NotNull
	private final RenderType type;

	@NotNull
	private final List<ItemStack> items;

	public @NotNull UUID capture() {
		final UUID id = UUID.randomUUID();

		final List<ItemStack> filteredItems = items.stream()
				.map(item -> item != null ? item : new ItemStack(org.bukkit.Material.AIR))
				.toList();

		final RenderEntry entry = new RenderEntry(
				id,
				owner,
				type,
				filteredItems
		);

		return RenderService.get().capture(entry);
	}
}