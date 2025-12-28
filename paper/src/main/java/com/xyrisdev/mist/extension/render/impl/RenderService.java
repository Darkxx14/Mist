package com.xyrisdev.mist.extension.render.impl;

import com.xyrisdev.library.menu.Menu;
import com.xyrisdev.mist.extension.render.impl.cache.RenderCache;
import com.xyrisdev.mist.extension.render.impl.object.RenderEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class RenderService {

	private static final RenderService INSTANCE = new RenderService();
	private final RenderCache cache = new RenderCache();

	public @NotNull UUID capture(@NotNull RenderEntry entry) {
		this.cache.put(entry);
		return entry.id();
	}

	public void invalidate(@NotNull Player player) {
		this.cache.invalidate(player);
	}

	public void render(@NotNull Player viewer, @NotNull UUID id) {
		final RenderEntry entry = this.cache.get(id);

		if (entry == null) {
			return;
		}

		final String name = entry.owner().getName();

		final Menu menu = switch (entry.type()) {
			case INVENTORY -> new Menu(54, Component.text(name + "'s Inventory"));
			case ENDER_CHEST -> new Menu(InventoryType.ENDER_CHEST, Component.text(name + "'s Ender Chest"));
			case SHULKER_BOX -> new Menu(InventoryType.SHULKER_BOX, Component.text(name + "'s Shulker Box"));
			case ITEM -> new Menu(9, Component.text(name + "'s Item"));
		};

		for (int i = 0; i < entry.items().size(); i++) {
			final ItemStack item = entry.items().get(i);

			if (item != null) {
				menu.item(i, item);
			}
		}

		menu.open(viewer);
	}

	public static @NotNull RenderService get() {
		return INSTANCE;
	}
}
