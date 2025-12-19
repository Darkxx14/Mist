package com.xyrisdev.mist.module.replacement.config.loader;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.module.replacement.config.ReplacementsConfiguration;
import com.xyrisdev.mist.module.replacement.entry.UnifiedReplacement;
import com.xyrisdev.mist.module.replacement.entry.type.PlaceholderAPIReplacement;
import com.xyrisdev.mist.module.replacement.entry.type.StaticReplacement;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class ReplacementsConfigurationLoader {

	public static @NotNull ReplacementsConfiguration load(@NotNull CachableConfiguration config) {
		final List<UnifiedReplacement> replacements = new ArrayList<>();
		final ConfigurationSection section = config.getSection("replacements");

		if (section != null) {
			for (String key : section.getKeys(false)) {
				final ConfigurationSection entry = section.getConfigurationSection(key);

				if (entry == null) {
					continue;
				}

				final String type = entry.getString("type");
				final String match = entry.getString("match");

				if (type == null || match == null) {
					continue;
				}

				switch (type.toUpperCase()) {
					case "STATIC" -> {
						final String replace = entry.getString("replace");

						if (replace != null && !replace.isBlank()) {
							replacements.add(new StaticReplacement(match, replace));
						}
					}
					case "PAPI" -> {
						final String processor = entry.getString("processor");
						final String permission = entry.getString("permission", "");

						if (processor != null && !processor.isBlank()) {
							replacements.add(new PlaceholderAPIReplacement(match, processor, permission));
						}
					}
				}
			}
		}

		return new ReplacementsConfiguration(replacements);
	}
}