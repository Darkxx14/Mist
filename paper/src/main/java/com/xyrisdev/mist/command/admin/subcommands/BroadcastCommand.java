package com.xyrisdev.mist.command.admin.subcommands;

import com.xyrisdev.mist.util.text.MistTextParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.parser.standard.StringParser;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class BroadcastCommand {

	public void register(@NotNull PaperCommandManager<Source> manager, Command.@NotNull Builder<Source> root) {
		manager.command(
				root.literal("broadcast")
						.permission("mist.commands.broadcast")
						.literal("chat")
						.required("message", StringParser.greedyStringParser())
						.handler(this::chat)
		);

		manager.command(
				root.literal("broadcast")
						.permission("mist.commands.broadcast")
						.literal("actionbar")
						.required("message", StringParser.greedyStringParser())
						.handler(this::actionBar)
		);

		manager.command(
				root.literal("broadcast")
						.permission("mist.commands.broadcast")
						.literal("title")
						.required("message", StringParser.greedyStringParser())
						.handler(this::title)
		);
	}

	private void chat(@NotNull CommandContext<Source> ctx) {
		final String input = ctx.get("message");

		Bukkit.getOnlinePlayers().forEach(player ->
				player.sendMessage(MistTextParser.parse(player, input))
		);
	}

	private void actionBar(@NotNull CommandContext<Source> ctx) {
		final String input = ctx.get("message");

		Bukkit.getOnlinePlayers().forEach(player ->
				player.sendActionBar(MistTextParser.parse(player, input))
		);
	}

	private void title(@NotNull CommandContext<Source> ctx) {
		final String[] parts = ctx.<String>get("message").split(":", 2);

		Bukkit.getOnlinePlayers().forEach(player -> {
			final Component title = MistTextParser.parse(player, parts[0]);
			final Component subtitle = parts.length > 1
					? MistTextParser.parse(player, parts[1])
					: Component.empty();

			player.showTitle(
					Title.title(
							title,
							subtitle,
							Title.Times.times(
									Duration.ofMillis(300),
									Duration.ofSeconds(2),
									Duration.ofMillis(300)
							)
					)
			);
		});
	}
}
