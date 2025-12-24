package com.xyrisdev.mist.util.regex;

import com.xyrisdev.mist.util.matcher.LeetMap;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class RegexGenerator {

	private static volatile Map<Character, String> REVERSE_LEET = Map.of();

	public static void index() {
		final Map<Character, Set<Character>> temp = new HashMap<>();

		for (char c = 32; c < 127; c++) {
			final char mapped = LeetMap.map(c);

			if (mapped == c) {
				continue;
			}

			temp.computeIfAbsent(mapped, __ -> new HashSet<>()).add(c);
		}

		final Map<Character, String> resolved = new HashMap<>();

		for (Map.Entry<Character, Set<Character>> e : temp.entrySet()) {
			final StringBuilder set = new StringBuilder();

			set.append(e.getKey());

			for (char variant : e.getValue()) {
				set.append(escape(variant));
			}

			resolved.put(e.getKey(), set.toString());
		}

		REVERSE_LEET = resolved;
	}

	public static @NotNull String generate(@NotNull String input) {
		if (input.isEmpty()) {
			return "(?i)";
		}

		final StringBuilder output = new StringBuilder("(?i)\\b");

		char last = 0;

		for (char raw : input.toLowerCase().toCharArray()) {
			if (Character.isWhitespace(raw)) {
				output.append("\\s+");
				last = 0;
				continue;
			}

			if (!Character.isLetterOrDigit(raw)) {
				output.append("\\Q").append(raw).append("\\E");
				last = raw;
				continue;
			}

			if (raw == last) {
				output.append("+");
				continue;
			}

			last = raw;

			final String variants = REVERSE_LEET.get(raw);

			if (variants != null) {
				output.append('[').append(variants).append(']');
			} else {
				output.append('[').append(raw).append(']');
			}
		}

		output.append("\\b");
		return output.toString();
	}

	private static char escape(char ch) {
		return switch (ch) {
			case '\\', '-', '^', ']' -> '\\';
			default -> ch;
		};
	}
}
