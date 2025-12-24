package com.xyrisdev.mist.util.regex;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.text.Normalizer;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

//todo: needs a recode
@UtilityClass
public class RegexGenerator {

	public enum Level {
		BASIC,
		AGGRESSIVE,
		AGGRESSIVE_V2
	}

	private static final Set<Character> REPEATABLE =
			Set.of('a', 'e', 'i', 'o', 'u', 's');

	private static final Map<Character, String> BASIC_MAP = Map.ofEntries(
			Map.entry('a', "[a@4]"),
			Map.entry('e', "[e3]"),
			Map.entry('i', "[i1!]"),
			Map.entry('o', "[o0]"),
			Map.entry('s', "[s$5]"),
			Map.entry('t', "[t7]")
	);

	private static final Map<Character, String> AGGRESSIVE_MAP = Map.ofEntries(
			Map.entry('a', "[a@4àáâãäåα]"),
			Map.entry('b', "[b8ß]"),
			Map.entry('c', "[cçk(¢]"),
			Map.entry('d', "[dđ]"),
			Map.entry('e', "[e3€èéêëε]"),
			Map.entry('f', "[fƒ]"),
			Map.entry('g', "[g69]"),
			Map.entry('h', "[h#ħ]"),
			Map.entry('i', "[i1!|ìíîïι]"),
			Map.entry('k', "[kκ]"),
			Map.entry('l', "[l1|!ɩ]"),
			Map.entry('n', "[nñη]"),
			Map.entry('o', "[o0°òóôõöο]"),
			Map.entry('p', "[pρ]"),
			Map.entry('s', "[s$5§]"),
			Map.entry('t', "[t7+τ]"),
			Map.entry('u', "[uµvυ]"),
			Map.entry('y', "[y¥γ]"),
			Map.entry('z', "[z2]")
	);

	public static @NotNull Pattern generate(@NotNull String word, @NotNull Level level) {
		final String normalized = Normalizer.normalize(word, Normalizer.Form.NFKC).toLowerCase();
		final Map<Character, String> map = level == Level.BASIC ? BASIC_MAP : AGGRESSIVE_MAP;

		final String separator = level == Level.AGGRESSIVE_V2
						? "[\\p{Z}\\p{C}\\W_]*+"
						: level == Level.AGGRESSIVE
						? "[\\W_]*+"
						: "[\\W_]*";

		final StringBuilder regex = new StringBuilder(128).append("(?<![\\p{L}\\p{N}])");

		for (char c : normalized.toCharArray()) {
			if (!Character.isLetterOrDigit(c)) {
				continue;
			}

			final String token = map.getOrDefault(c, Pattern.quote(String.valueOf(c)));

			final String repeat = level == Level.AGGRESSIVE_V2
							? (REPEATABLE.contains(c) ? "{1,8}" : "{1,3}")
							: level == Level.AGGRESSIVE
							? (REPEATABLE.contains(c) ? "{1,6}" : "{1,2}")
							: (REPEATABLE.contains(c) ? "{1,3}" : "{1,1}");

			regex.append("(?:").append(token).append(")").append(repeat);
			regex.append(separator);
		}

		regex.append("(?![\\p{L}\\p{N}])");

		return Pattern.compile(
				regex.toString(),
				Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
		);
	}
}
