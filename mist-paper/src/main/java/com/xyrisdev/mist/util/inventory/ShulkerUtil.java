package com.xyrisdev.mist.util.inventory;

import lombok.experimental.UtilityClass;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public final class ShulkerUtil {

	public static @NotNull List<ItemStack> contents(@NotNull ItemStack shulker) {
		if (!(shulker.getItemMeta() instanceof BlockStateMeta meta)) {
			return List.of();
		}

		if (!(meta.getBlockState() instanceof ShulkerBox box)) {
			return List.of();
		}

		final ItemStack[] contents = box.getInventory().getContents();
		final List<ItemStack> items = new ArrayList<>(contents.length);

		for (ItemStack item : contents) {
			items.add(item != null ? item.clone() : null);
		}

		return items;
	}
}
