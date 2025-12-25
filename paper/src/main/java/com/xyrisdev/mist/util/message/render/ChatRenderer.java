package com.xyrisdev.mist.util.message.render;

import com.xyrisdev.mist.util.message.builder.object.MessageContext;
import com.xyrisdev.mist.util.text.TextParser;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@UtilityClass
public class ChatRenderer {

	public static void render(
			@NotNull Audience audience, @Nullable Player player,
			@Nullable ConfigurationSection section, @NotNull MessageContext ctx
	) {
		if (section == null) {
			return;
		}

		if (section.isList("list")) {
			final List<String> lines = section.getStringList("list");
			if (lines.isEmpty()) {
				return;
			}

			final Component combined = Component.join(
					JoinConfiguration.separator(Component.newline()),
					lines.stream()
							.map(line -> parse(audience, player, line))
							.map(ctx::apply)
							.toList()
			);

			audience.sendMessage(combined);
			return;
		}

		final String text = section.getString("text");

		if (text != null) {
			audience.sendMessage(
					ctx.apply(
							parse(audience, player, text)
					)
			);
		}
	}

	private static @NotNull Component parse(@NotNull Audience audience, @Nullable Player player, @NotNull String input) {
		return TextParser.parse(
				player != null ? player : audience,
				input
		);
	}
}
