package com.xyrisdev.mist.util.text;

import com.xyrisdev.mist.util.text.tags.registry.TagRegistry;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class MistTextParser {

	private static final TagRegistry TAG_REGISTRY = new TagRegistry();
	private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

	public static @NotNull Component parse(@NotNull Audience audience, @NotNull String input) {
		final String message = containsLegacy(input)
				? legacy(input)
				: input;

		return MINI_MESSAGE.deserialize(
				message,
				TAG_REGISTRY.build(audience)
		);
	}

	private static boolean containsLegacy(@NotNull String input) {
		return input.indexOf('&') != -1 || input.indexOf('§') != -1;
	}

	// hacky way to preserve custom tags ):
	private static @NotNull String legacy(final @NotNull String input) {
		String result = input;

		result = result.replaceAll("&#([A-Fa-f0-9]{6})", "<#$1>");
		result = result.replaceAll("§#([A-Fa-f0-9]{6})", "<#$1>");

		result = result.replace("&0", "<black>")
				.replace("&1", "<dark_blue>")
				.replace("&2", "<dark_green>")
				.replace("&3", "<dark_aqua>")
				.replace("&4", "<dark_red>")
				.replace("&5", "<dark_purple>")
				.replace("&6", "<gold>")
				.replace("&7", "<gray>")
				.replace("&8", "<dark_gray>")
				.replace("&9", "<blue>")
				.replace("&a", "<green>")
				.replace("&b", "<aqua>")
				.replace("&c", "<red>")
				.replace("&d", "<light_purple>")
				.replace("&e", "<yellow>")
				.replace("&f", "<white>")
				.replace("&k", "<obfuscated>")
				.replace("&l", "<bold>")
				.replace("&m", "<strikethrough>")
				.replace("&n", "<underlined>")
				.replace("&o", "<italic>")
				.replace("&r", "<reset>");

		result = result.replace("§0", "<black>")
				.replace("§1", "<dark_blue>")
				.replace("§2", "<dark_green>")
				.replace("§3", "<dark_aqua>")
				.replace("§4", "<dark_red>")
				.replace("§5", "<dark_purple>")
				.replace("§6", "<gold>")
				.replace("§7", "<gray>")
				.replace("§8", "<dark_gray>")
				.replace("§9", "<blue>")
				.replace("§a", "<green>")
				.replace("§b", "<aqua>")
				.replace("§c", "<red>")
				.replace("§d", "<light_purple>")
				.replace("§e", "<yellow>")
				.replace("§f", "<white>")
				.replace("§k", "<obfuscated>")
				.replace("§l", "<bold>")
				.replace("§m", "<strikethrough>")
				.replace("§n", "<underlined>")
				.replace("§o", "<italic>")
				.replace("§r", "<reset>");

		return result;
	}
}