package com.xyrisdev.mist.module.format.config.loader;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.module.format.config.FormatConfiguration;
import com.xyrisdev.mist.module.format.entry.ClickAction;
import com.xyrisdev.mist.module.format.entry.FormatEntry;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FormatConfigurationLoader {

	private static final String KEY = "formats";
	private static final String DEFAULT_FALLBACK = "default";

	public static @NotNull FormatConfiguration load(
			@NotNull CachableConfiguration config
	) {
		final String fallback =
				config.getString("fallback_format", DEFAULT_FALLBACK);

		final Map<String, FormatEntry> formats = new HashMap<>();
		final ConfigurationSection formatsSection = config.getSection(KEY);

		if (formatsSection == null) {
			return new FormatConfiguration(formats, fallback);
		}

		for (String key : formatsSection.getKeys(false)) {
			final String path = KEY + "." + key;
			final String message = config.getString(path + ".message");

			if (message == null || message.isBlank()) {
				continue;
			}

			formats.put(
					key,
					new FormatEntry(
							message,
							config.getStringList(path + ".hover_text"),
							action(config, path + ".action")
					)
			);
		}

		return new FormatConfiguration(formats, fallback);
	}

	private static @Nullable ClickAction action(
			@NotNull CachableConfiguration config,
			@NotNull String path
	) {
		final String type =
				config.getString(path + ".action");

		final String value =
				config.getString(path + ".value");

		if (type == null || value == null) {
			return null;
		}

		try {
			return new ClickAction(
					ClickEvent.Action.valueOf(type),
					value
			);
		} catch (IllegalArgumentException ignored) {
			return null;
		}
	}
}
