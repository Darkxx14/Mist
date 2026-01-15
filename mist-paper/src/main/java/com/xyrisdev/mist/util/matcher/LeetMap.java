package com.xyrisdev.mist.util.matcher;

import com.xyrisdev.mist.config.ConfigType;
import com.xyrisdev.mist.config.registry.ConfigRegistry;
import com.xyrisdev.mist.util.logger.MistLogger;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class LeetMap {

	private static Map<Character, Character> map = Map.of();

	public static void load(@NotNull ConfigRegistry registry) {
		final ConfigurationSection section = registry.get(ConfigType.LEETMAP).getSection("mappings");

		if (section == null) {
			map = Map.of();
			return;
		}

		final Map<Character, Character> resolved = new HashMap<>();

		for (String normalized : section.getKeys(false)) {

			if (normalized.length() != 1) {
				continue;
			}

			final char target = Character.toLowerCase(normalized.charAt(0));
			final List<String> values = section.getStringList(normalized);

			for (String raw : values) {

				if (raw.length() != 1) {
					continue;
				}

				final char charAt = raw.charAt(0);

				if (Character.isLetter(charAt)) {
					continue;
				}

				resolved.put(charAt, target);
			}
		}

		map = Collections.unmodifiableMap(resolved);
		MistLogger.info("Loaded " + map.size() + " leet mappings");
	}

	public static char map(char input) {
		if (Character.isLetter(input)) {
			return input;
		}

		return map.getOrDefault(input, input);
	}

	public static @NotNull String map(@NotNull String input) {
		final StringBuilder output = new StringBuilder(input.length());

		for (char c : input.toCharArray()) {
			output.append(map(c));
		}

		return output.toString();
	}
}
