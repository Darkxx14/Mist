package com.xyrisdev.mist.util.message.render;

import com.xyrisdev.mist.util.message.builder.object.MessageContext;
import com.xyrisdev.mist.util.text.TextParser;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
			for (String line : section.getStringList("list")) {
				audience.sendMessage(
						ctx.apply(
								parse(audience, player, line)
						)
				);
			}
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