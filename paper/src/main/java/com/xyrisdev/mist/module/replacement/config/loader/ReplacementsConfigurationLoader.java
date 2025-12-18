package com.xyrisdev.mist.module.replacement.config.loader;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.module.replacement.config.ReplacementsConfiguration;
import com.xyrisdev.mist.module.replacement.entry.PlaceholderAPIReplacement;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class ReplacementsConfigurationLoader {

	public static @NotNull ReplacementsConfiguration load(@NotNull CachableConfiguration config) {
		final Map<String, String> replacements = new HashMap<>();
		final Map<String, PlaceholderAPIReplacement> papiReplacements = new HashMap<>();

		final ConfigurationSection replacementsSection = config.getSection("replacements");

		if (replacementsSection != null) {
			for (String key : replacementsSection.getKeys(false)) {
				final String value = replacementsSection.getString(key);

				if (value != null && !value.isBlank()) {
					replacements.put(key, value);
				}

			}
		}

		final ConfigurationSection papiSection = config.getSection("placeholderapi_replacements");

		if (papiSection != null) {
			for (String id : papiSection.getKeys(false)) {
				final ConfigurationSection entry = papiSection.getConfigurationSection(id);

				if (entry == null) {
					continue;
				}

				final String key = entry.getString("key");
				final String processor = entry.getString("processor");

				if (key == null || processor == null) {
					continue;
				}

				papiReplacements.put(
						id,
						new PlaceholderAPIReplacement(
								key,
								processor,
								entry.getBoolean("require_permission", false),
								entry.getString("permission", "")
						)
				);
			}
		}

		return new ReplacementsConfiguration(replacements, papiReplacements);
	}
}
