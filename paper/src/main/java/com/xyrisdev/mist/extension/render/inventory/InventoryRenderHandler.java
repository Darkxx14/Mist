package com.xyrisdev.mist.extension.render.inventory;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.extension.ExtensionHandler;
import com.xyrisdev.mist.extension.render.common.RenderHandler;
import com.xyrisdev.mist.extension.render.config.RenderConfiguration;
import com.xyrisdev.mist.extension.render.inventory.impl.object.InventoryRenderRequest;
import com.xyrisdev.mist.extension.render.inventory.impl.object.InventoryRenderType;
import com.xyrisdev.mist.util.text.TextParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.atteo.evo.inflector.English;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// todo: make menus configurable
public class InventoryRenderHandler implements RenderHandler {

	private final RenderConfiguration config;

	public InventoryRenderHandler(@NotNull RenderConfiguration config) {
		this.config = config;
	}

	@ExtensionHandler
	public void handle(@NotNull ChatContext ctx) {
		final String message = ctx.plain();
		final Player player = ctx.sender();

		if (this.config.inventory().enabled()) {
			for (String prefix : this.config.inventory().prefix()) {
				if (message.contains(prefix)) {
					render(ctx, player, InventoryRenderType.INVENTORY, this.config.inventory().processor(), prefix);
				}
			}
		}

		if (this.config.item().enabled()) {
			for (String prefix : this.config.item().prefix()) {
				if (message.contains(prefix)) {
					render(ctx, player, InventoryRenderType.ITEM, this.config.item().processor(), prefix);
				}
			}
		}

		if (this.config.enderChest().enabled()) {
			for (String prefix : this.config.enderChest().prefix()) {
				if (message.contains(prefix)) {
					render(ctx, player, InventoryRenderType.ENDER_CHEST, this.config.enderChest().processor(), prefix);
				}
			}
		}

		if (this.config.shulkerBox().enabled()) {
			for (String prefix : this.config.shulkerBox().prefix()) {
				if (message.contains(prefix)) {
					render(ctx, player, InventoryRenderType.SHULKER_BOX, this.config.shulkerBox().processor(), prefix);
				}
			}
		}
	}

	private void render(@NotNull ChatContext ctx, @NotNull Player player, @NotNull InventoryRenderType type, @NotNull String processor, @NotNull String prefix) {
		final List<ItemStack> items = collect(player, type);

		final UUID id = InventoryRenderRequest.builder()
				.owner(player)
				.type(type)
				.items(items)
				.build()
				.capture();

		final List<String> hoverText = switch (type) {
			case INVENTORY -> this.config.inventory().hoverText();
			case ITEM -> List.of();
			case ENDER_CHEST -> this.config.enderChest().hoverText();
			case SHULKER_BOX -> this.config.shulkerBox().hoverText();
		};

		final Component display = displayComponent(
				player,
				type,
				processor,
				id,
				items,
				hoverText
		);

		ctx.message(ctx.message().replaceText(builder ->
				builder.matchLiteral(prefix).replacement(display)
		));
	}

	private @NotNull List<ItemStack> collect(@NotNull Player player, @NotNull InventoryRenderType type) {
		return switch (type) {
			case INVENTORY -> {
				final List<ItemStack> items = new ArrayList<>();

				for (ItemStack item : player.getInventory().getContents()) {
					items.add(item != null ? item.clone() : null);
				}

				yield items;
			}

			case ITEM -> List.of(player.getInventory().getItemInMainHand().clone());

			case ENDER_CHEST -> {
				final List<ItemStack> items = new ArrayList<>();

				for (ItemStack item : player.getEnderChest().getContents()) {
					items.add(item != null ? item.clone() : null);
				}

				yield items;
			}

			case SHULKER_BOX -> {
				final ItemStack hand = player.getInventory().getItemInMainHand();

				if (hand.getType().name().contains("SHULKER_BOX")) {
					yield shulkerContents(hand);
				}

				yield List.of();
			}
		};
	}

	private @NotNull List<ItemStack> shulkerContents(@NotNull ItemStack shulker) {
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

	private @NotNull Component displayComponent(
			@NotNull Player player, @NotNull InventoryRenderType type,
			@NotNull String processor, @NotNull UUID id,
			@NotNull List<ItemStack> items, @NotNull List<String> hoverText
	) {
		Component result = TextParser.parse(player, processor)
				.clickEvent(
						ClickEvent.runCommand("/mistcallback " + id)
				);

		if (type == InventoryRenderType.ITEM && !items.isEmpty()) {
			final ItemStack item = items.getFirst();

			if (item != null && !item.getType().isAir()) {
				final int amount = item.getAmount();

				final Component itemName =
						item.hasItemMeta() && item.getItemMeta().hasDisplayName()
								? item.getItemMeta().displayName()
								: Component.text(
								English.plural(
										Arrays.stream(item.getType().name().split("_"))
												.map(word -> word.charAt(0) + word.substring(1).toLowerCase())
												.collect(Collectors.joining(" ")),
										amount
								)
						);

				result = result.replaceText(builder ->
						builder.matchLiteral("<item>").replacement(itemName)
				);

				return result.hoverEvent(item);
			}
		}

		if (!hoverText.isEmpty()) {
			final Component hover = Component.join(
					JoinConfiguration.separator(Component.newline()),
					hoverText.stream()
							.map(line -> TextParser.parse(player, line))
							.toList()
			);

			result = result.hoverEvent(HoverEvent.showText(hover));
		}

		return result;
	}
}