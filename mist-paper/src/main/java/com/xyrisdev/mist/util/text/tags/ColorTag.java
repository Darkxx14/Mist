package com.xyrisdev.mist.util.text.tags;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.Modifying;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.Objects;

public record ColorTag(@NotNull TextColor color) implements Modifying {

	// palette from github.com/nulli0n/nightcore-spigot
	private static final Map<String, TextColor> PALETTE = Map.ofEntries(
			color("m_black", "#000000"),
			color("m_white", "#ffffff"),
			color("m_gray", "#aaa8a8"),
			color("m_green", "#74ea31"),
			color("m_yellow", "#ead931"),
			color("m_orange", "#ea9631"),
			color("m_red", "#ea3131"),
			color("m_blue", "#3196ea"),
			color("m_cyan", "#31eace"),
			color("m_purple", "#bd31ea"),
			color("m_pink", "#ea31b2"),

			color("m_light_gray", "#d4d9d8"),
			color("m_light_green", "#91f251"),
			color("m_light_yellow", "#ffeea2"),
			color("m_light_orange", "#fdba5e"),
			color("m_light_red", "#fd5e5e"),
			color("m_light_blue", "#5e9dfd"),
			color("m_light_cyan", "#5edefd"),
			color("m_light_purple", "#e39fff"),
			color("m_light_pink", "#fd8ddb"),

			color("m_dark_gray", "#6c6c62")
	);

	@Override
	public @NotNull Component apply(@NotNull Component current, int depth) {
		if (depth != 0) {
			return Component.empty();
		}

		return current.color(this.color);
	}

	public static @NotNull TagResolver resolver() {
		final TagResolver.Builder builder = TagResolver.builder();

		PALETTE.forEach((name, color) ->
				builder.tag(name, (args, ctx) -> new ColorTag(color))
		);

		return builder.build();
	}

	@Contract("_, _ -> new")
	private static Map.@NotNull @Unmodifiable Entry<String, TextColor> color(@NotNull String name, @NotNull String hex) {
		return Map.entry(name, requireColor(hex, name));
	}

	private static @NotNull TextColor requireColor(@NotNull String hex, @NotNull String name) {
		return Objects.requireNonNull(TextColor.fromHexString(hex),
				() -> "Invalid hex color '" + hex + "' for tag '" + name + "'"
		);
	}
}
