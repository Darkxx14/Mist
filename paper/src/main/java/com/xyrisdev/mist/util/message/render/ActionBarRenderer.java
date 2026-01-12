package com.xyrisdev.mist.util.message.render;

import com.xyrisdev.mist.util.message.builder.object.MessageContext;
import com.xyrisdev.mist.util.text.TextParser;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class ActionBarRenderer {

	public static void render(@NotNull Player player, @Nullable ConfigurationSection section, @NotNull MessageContext ctx) {
		if (section == null) {
			return;
		}

		final String text = section.getString("text");

		if (empty(text)) {
			return;
		}

		player.sendActionBar(
				ctx.apply(TextParser.parse(player, text))
		);
	}

	private static boolean empty(@Nullable String value) {
		return value == null || value.equalsIgnoreCase("<empty>");
	}
}