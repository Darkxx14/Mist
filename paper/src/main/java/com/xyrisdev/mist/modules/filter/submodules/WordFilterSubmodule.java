package com.xyrisdev.mist.modules.filter.submodules;

import com.xyrisdev.mist.modules.filter.config.WordFilterConfig;
import com.xyrisdev.mist.modules.filter.object.FilterResult;
import com.xyrisdev.mist.modules.filter.object.FilterSubmodule;
import com.xyrisdev.mist.util.SimilarityUtil;
import org.jetbrains.annotations.NotNull;

import java.text.Normalizer;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

public final class WordFilterSubmodule implements FilterSubmodule {

	private final WordFilterConfig config;

	public WordFilterSubmodule(WordFilterConfig config) {
		this.config = config;
	}

	@Override
	public @NotNull FilterResult handle(@NotNull UUID senderId, @NotNull String message) {
		final String normalized = normalize(message);

		for (String word : config.blockedWords()) {
			int score = 0;

			// 1 - normalized contains
			if (normalized.contains(word)) {
				score++;
			}

			// 2 - regex spacing / symbol bypass
			if (Pattern.compile(buildRegex(word)).matcher(message).find()) {
				score++;
			}

			// 3 - similarity
			if (similarityMatch(normalized, word)) {
				score++;
			}

			if (score >= 2) {
				return act(message);
			}
		}

		return FilterResult.pass();
	}

	private FilterResult act(String message) {
		if (config.cancelMessage()) {
			return FilterResult.block();
		}

		final String replaced = replace(message, config.replaceWith());
		return FilterResult.modify(replaced);
	}

	private static @NotNull String normalize(String input) {
		final String s = Normalizer.normalize(input, Normalizer.Form.NFKD)
				.replaceAll("\\p{M}", "")
				.replaceAll("[^\\p{L}\\p{N}]", "")
				.toLowerCase(Locale.ROOT);

		return s.replaceAll("(.)\\1{2,}", "$1$1");
	}

	private boolean similarityMatch(@NotNull String message, @NotNull String word) {
		for (int i = 0; i <= message.length() - word.length(); i++) {
			final String slice = message.substring(i, i + word.length());

			if (SimilarityUtil.similarity(slice, word) >= config.similarityThreshold()) {
				return true;
			}
		}

		return false;
	}

	private @NotNull String buildRegex(@NotNull String word) {
		final StringBuilder out = new StringBuilder("(?i)");

		for (char c : word.toCharArray()) {
			out.append('[').append(c).append("]+\\W*");
		}

		return out.toString();
	}

	private @NotNull String replace(@NotNull String message, char mask) {
		return message.replaceAll("\\.", String.valueOf(mask));
	}

	@Override
	public int priority() {
		return 4;
	}

	@Override
	public boolean enabled() {
		return config.enable();
	}
}
