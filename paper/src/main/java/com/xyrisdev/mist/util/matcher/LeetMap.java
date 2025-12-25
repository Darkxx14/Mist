package com.xyrisdev.mist.util.matcher;

import com.xyrisdev.mist.util.config.ConfigType;
import com.xyrisdev.mist.util.config.registry.ConfigRegistry;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class LeetMap {

	private static volatile Map<Character, Character> MAP = Map.of();

	public static void load(@NotNull ConfigRegistry registry) {
		final ConfigurationSection section = registry.get(ConfigType.LEETMAP).getSection("mappings");

		if (section == null) {
			MAP = Map.of();
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

				final char source = raw.charAt(0);

				if (Character.isLetter(source)) {
					continue;
				}

				resolved.put(source, target);
			}
		}

		MAP = Collections.unmodifiableMap(resolved);
	}

	public static char map(char input) {
		if (Character.isLetter(input)) {
			return input;
		}

		return MAP.getOrDefault(input, input);
	}

	public static @NotNull String map(@NotNull String input) {
		final StringBuilder out = new StringBuilder(input.length());

		for (char c : input.toCharArray()) {
			out.append(map(c));
		}

		return out.toString();
	}
}
