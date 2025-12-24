package com.xyrisdev.mist.extension.render.stage;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessStage;
import com.xyrisdev.mist.extension.render.config.RenderConfiguration;
import com.xyrisdev.mist.extension.render.impl.object.RenderRequest;
import com.xyrisdev.mist.extension.render.impl.object.RenderType;
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

public class RenderStage implements ChatProcessStage {

	private final RenderConfiguration config;

	public RenderStage(@NotNull RenderConfiguration config) {
		this.config = config;
	}

	@Override
	public void process(@NotNull ChatContext context) {
		final String message = context.plain();
		final Player player = context.sender();

		if (config.inventory().enable()) {
			for (String prefix : config.inventory().prefix()) {
				if (message.contains(prefix)) {
					render(context, player, RenderType.INVENTORY, config.inventory().processor(), prefix);
					return;
				}
			}
		}

		if (config.item().enable()) {
			for (String prefix : config.item().prefix()) {
				if (message.contains(prefix)) {
					render(context, player, RenderType.ITEM, config.item().processor(), prefix);
					return;
				}
			}
		}

		if (config.enderChest().enable()) {
			for (String prefix : config.enderChest().prefix()) {
				if (message.contains(prefix)) {
					render(context, player, RenderType.ENDER_CHEST, config.enderChest().processor(), prefix);
					return;
				}
			}
		}

		if (config.shulkerBox().enable()) {
			for (String prefix : config.shulkerBox().prefix()) {
				if (message.contains(prefix)) {
					render(context, player, RenderType.SHULKER_BOX, config.shulkerBox().processor(), prefix);
					return;
				}
			}
		}
	}

	private void render(@NotNull ChatContext context, @NotNull Player player, @NotNull RenderType type, @NotNull String processor, @NotNull String prefix) {
		final List<ItemStack> items = collect(player, type);

		final UUID id = RenderRequest.builder()
				.owner(player)
				.type(type)
				.items(items)
				.build()
				.capture();

		final List<String> hoverText = switch (type) {
			case INVENTORY -> config.inventory().hoverText();
			case ITEM -> List.of();
			case ENDER_CHEST -> config.enderChest().hoverText();
			case SHULKER_BOX -> config.shulkerBox().hoverText();
		};

		final Component display = displayComponent(
				player,
				type,
				processor,
				id,
				items,
				hoverText
		);

		context.message(context.message().replaceText(builder ->
				builder.matchLiteral(prefix).replacement(display)
		));
	}

	private @NotNull List<ItemStack> collect(@NotNull Player player, @NotNull RenderType type) {
		return switch (type) {
			case INVENTORY -> {
				List<ItemStack> items = new ArrayList<>();
				for (ItemStack item : player.getInventory().getContents()) {
					items.add(item != null ? item.clone() : null);
				}
				yield items;
			}

			case ITEM -> List.of(player.getInventory().getItemInMainHand().clone());

			case ENDER_CHEST -> {
				List<ItemStack> items = new ArrayList<>();
				for (ItemStack item : player.getEnderChest().getContents()) {
					items.add(item != null ? item.clone() : null);
				}
				yield items;
			}

			case SHULKER_BOX -> {
				ItemStack hand = player.getInventory().getItemInMainHand();
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
			@NotNull Player player, @NotNull RenderType type,
			@NotNull String processor, @NotNull UUID id,
			@NotNull List<ItemStack> items, @NotNull List<String> hoverText
	) {
		Component result = TextParser.parse(player, processor)
				.clickEvent(ClickEvent.runCommand("/mistcallback " + id));

		if (type == RenderType.ITEM && !items.isEmpty()) {
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