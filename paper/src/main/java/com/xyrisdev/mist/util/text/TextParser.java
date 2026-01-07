package com.xyrisdev.mist.util.text;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.xyrisdev.mist.util.text.cache.TextCacheKey;
import com.xyrisdev.mist.util.text.tags.registry.TagRegistry;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Objects;

@UtilityClass
public class TextParser {

	private static final TagRegistry tagRegistry = new TagRegistry();
	private static final MiniMessage mm = MiniMessage.miniMessage();

	private static final Cache<TextCacheKey, Component> cache = Caffeine.newBuilder()
			.maximumSize(10_000)
			.expireAfterAccess(Duration.ofMinutes(5))
			.build();

	public static @NotNull Component parse(@NotNull Audience audience, @NotNull String input) {
		final String message = containsLegacy(input)
				? legacy(input)
				: input;


		return Objects.requireNonNull(
				cache.get(
						new TextCacheKey(audience, message),
						key -> mm.deserialize(
								key.message(),
								tagRegistry.build(key.audience())
						)
				),
				"MiniMessage returned null Component"
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