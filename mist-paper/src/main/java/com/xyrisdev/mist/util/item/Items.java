package com.xyrisdev.mist.util.item;

import com.xyrisdev.mist.util.item.builder.ItemBuilder;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
@UtilityClass
public final class Items {

	public static ItemBuilder of(Material material) {
		return new ItemBuilder(new ItemStack(material));
	}

	public static ItemBuilder of(ItemStack stack) {
		return new ItemBuilder(stack);
	}
}
