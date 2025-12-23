package com.xyrisdev.mist.command.admin.subcommands;

import com.xyrisdev.mist.util.text.MistTextParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class BroadcastCommand {

	public static void chat(@NotNull CommandContext<Source> ctx) {
		final String input = ctx.get("message");

		Bukkit.getOnlinePlayers().forEach(player ->
				player.sendMessage(MistTextParser.parse(player, input))
		);
	}

	public static void actionBar(@NotNull CommandContext<Source> ctx) {
		final String input = ctx.get("message");

		Bukkit.getOnlinePlayers().forEach(player ->
				player.sendActionBar(MistTextParser.parse(player, input))
		);
	}

	public static void title(@NotNull CommandContext<Source> ctx) {
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
