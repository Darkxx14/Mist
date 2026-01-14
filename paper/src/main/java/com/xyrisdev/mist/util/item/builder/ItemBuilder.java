package com.xyrisdev.mist.util.item.builder;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.*;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
@SuppressWarnings({"unused", "UnstableApiUsage"})
public record ItemBuilder(ItemStack stack) {

	public ItemStack build() {
		return this.stack;
	}

	public <T> ItemBuilder valuedComponent(DataComponentType.Valued<T> type, T value) {
		this.stack.setData(type, value);
		return this;
	}

	public ItemBuilder nonValuedComponent(DataComponentType.NonValued type) {
		this.stack.setData(type);
		return this;
	}

	public ItemBuilder reset(DataComponentType type) {
		this.stack.resetData(type);
		return this;
	}

	public ItemBuilder remove(DataComponentType type) {
		this.stack.unsetData(type);
		return this;
	}

	// some common stuff
	public ItemBuilder name(Component name) {
		return this.valuedComponent(DataComponentTypes.ITEM_NAME, name);
	}

	public ItemBuilder lore(Component... lines) {
		return this.valuedComponent(
				DataComponentTypes.LORE,
				ItemLore.lore()
						.addLines(List.of(lines))
						.build()
		);
	}

	public ItemBuilder lore(List<Component> lines) {
		return this.valuedComponent(
				DataComponentTypes.LORE,
				ItemLore.lore()
						.addLines(lines)
						.build()
		);
	}

	public ItemBuilder glow() {
		return this.valuedComponent(
				DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE,
				true
		);
	}

	public ItemBuilder enchant(Enchantment enchantment, int level) {
		return this.valuedComponent(
				DataComponentTypes.ENCHANTMENTS,
				ItemEnchantments.itemEnchantments()
						.add(enchantment, level)
						.build()
		);
	}

	public ItemBuilder hideTooltip(DataComponentType... components) {
		return this.valuedComponent(
				DataComponentTypes.TOOLTIP_DISPLAY,
				TooltipDisplay.tooltipDisplay()
						.addHiddenComponents(components)
						.build()
		);
	}
}