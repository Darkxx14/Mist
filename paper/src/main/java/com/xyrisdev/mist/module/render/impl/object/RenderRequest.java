package com.xyrisdev.mist.module.render.impl.object;

import com.xyrisdev.mist.module.render.impl.RenderService;
import lombok.Builder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Builder
public final class RenderRequest {

	@NotNull
	private final Player owner;

	@NotNull
	private final RenderType type;

	@NotNull
	private final List<ItemStack> items;

	public @NotNull UUID capture() {
		final UUID id = UUID.randomUUID();

		final RenderEntry entry = new RenderEntry(
				id,
				owner,
				type,
				List.copyOf(items)
		);

		return RenderService.get().capture(entry);
	}
}