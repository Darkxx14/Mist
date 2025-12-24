package com.xyrisdev.mist.extension.filter.rule.impl;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.extension.filter.rule.FilterResult;
import com.xyrisdev.mist.extension.filter.rule.FilterRule;
import com.xyrisdev.mist.util.matcher.LeetMap;
import com.xyrisdev.mist.util.message.MistMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.text.Normalizer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BlockedWordsRule implements FilterRule {

	private final Map<String, Entry> entries = new ConcurrentHashMap<>();

	@Override
	public @NotNull String key() {
		return "blocked_words";
	}

	@Override
	public int priority() {
		return 1;
	}

	@Override
	public void load(@NotNull ConfigurationSection section) {
		entries.clear();

		final ConfigurationSection words = section.getConfigurationSection("words");

		if (words == null) {
			return;
		}

		for (String raw : words.getKeys(false)) {
			final ConfigurationSection word = words.getConfigurationSection(raw);

			if (word == null) {
				continue;
			}

			final String normalized = normalize(raw);

			entries.put(
					normalized,
					new Entry(
							word.getBoolean("cancel", true),
							word.getString("replace", "***"),
							word.getBoolean("detect_in_words", true)
					)
			);
		}
	}

	@Override
	public @NotNull FilterResult process(@NotNull ChatContext ctx) {
		final String plain = ctx.plain();
		final String normalized = normalize(plain);

		for (Map.Entry<String, Entry> e : entries.entrySet()) {
			final String word = e.getKey();
			final Entry entry = e.getValue();

			final int index = normalized.indexOf(word);

			if (index == -1) {
				continue;
			}

			if (!entry.detectInWords && insideWord(normalized, word, index)) {
				continue;
			}

			if (entry.cancel) {
				MistMessage.create(ctx.sender())
						.id("modules.filtering.blocked_words.blocked")
						.send();
				return FilterResult.cancelled();
			}

			final int end = index + word.length();

			final String modified = plain.substring(0, index) + entry.replace + plain.substring(end);

			return FilterResult.modify(modified);
		}

		return FilterResult.pass();
	}

	private static boolean insideWord(String text, @NotNull String word, int start) {
		final int end = start + word.length();

		if (start > 0 && Character.isLetterOrDigit(text.charAt(start - 1))) {
			return true;
		}

		return end < text.length() && Character.isLetterOrDigit(text.charAt(end));
	}

	private static @NotNull String normalize(@NotNull String input) {
		if (input.isEmpty()) {
			return input;
		}

		String s = Normalizer.normalize(input, Normalizer.Form.NFKC);

		s = s.toLowerCase();
		s = s.replaceAll("\\p{M}+", "");
		s = LeetMap.map(s);
		s = s.replaceAll("[^a-z0-9]+", " ");

		return s.trim().replaceAll("\\s+", " ");
	}

	private record Entry(
			boolean cancel,
			@NotNull String replace,
			boolean detectInWords
	) {}
}
