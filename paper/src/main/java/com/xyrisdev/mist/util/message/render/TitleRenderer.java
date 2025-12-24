package com.xyrisdev.mist.util.message.render;

import com.xyrisdev.mist.util.message.builder.object.MessageContext;
import com.xyrisdev.mist.util.text.TextParser;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

@UtilityClass
public class TitleRenderer {

	public static void render(@NotNull Player player, @Nullable ConfigurationSection section, @NotNull MessageContext ctx) {
		if (section == null) {
			return;
		}

		final Component title = parse(
				player,
				ctx,
				section.getString("text")
		);

		final Component subtitle = parse(
				player,
				ctx,
				section.getString("subtitle")
		);

		final ConfigurationSection times = section.getConfigurationSection("times");

		final Title.Times titleTimes = times == null
				? Title.DEFAULT_TIMES
				: Title.Times.times(
				Duration.ofMillis(times.getInt("fade_in", 10) * 50L),
				Duration.ofMillis(times.getInt("stay", 40) * 50L),
				Duration.ofMillis(times.getInt("fade_out", 10) * 50L)
		);

		player.showTitle(Title.title(title, subtitle, titleTimes));
	}

	private static Component parse(@NotNull Player player, @NotNull MessageContext ctx, String input) {
		if (input == null || input.isBlank()) {
			return Component.empty();
		}

		return ctx.apply(
				TextParser.parse(player, input)
		);
	}
}
