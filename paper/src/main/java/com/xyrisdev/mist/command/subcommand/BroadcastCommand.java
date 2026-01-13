package com.xyrisdev.mist.command.subcommand;

import com.xyrisdev.mist.util.text.TextParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.incendo.cloud.annotation.specifier.Greedy;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;

@NullMarked
@SuppressWarnings("unused")
public class BroadcastCommand {

	@Command("mist broadcast chat <message>")
	@Permission("mist.command.broadcast")
	public void chat(@Greedy String message) {
		Bukkit.getOnlinePlayers().forEach(player ->
				player.sendMessage(TextParser.parse(player, message))
		);
	}

	@Command("mist broadcast actionbar <message>")
	@Permission("mist.command.broadcast")
	public void actionBar(@Greedy String message) {
		Bukkit.getOnlinePlayers().forEach(player ->
				player.sendActionBar(TextParser.parse(player, message))
		);
	}

	@Command("mist broadcast title <message>")
	@Permission("mist.command.broadcast")
	public void title(@Greedy String message) {
		final String[] parts = message.split(":", 2);

		Bukkit.getOnlinePlayers().forEach(player -> {
			final Component title = TextParser.parse(player, parts[0]);
			final Component subtitle = parts.length > 1 ? TextParser.parse(player, parts[1]) : Component.empty();

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
