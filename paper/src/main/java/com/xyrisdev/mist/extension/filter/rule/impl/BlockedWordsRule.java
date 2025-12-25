package com.xyrisdev.mist.extension.filter.rule.impl;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.extension.filter.rule.FilterResult;
import com.xyrisdev.mist.extension.filter.rule.FilterRule;
import com.xyrisdev.mist.util.matcher.LeetMap;
import com.xyrisdev.mist.util.message.MistMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.text.Normalizer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockedWordsRule implements FilterRule {

	private final Map<String, BlockedWordsEntry> entries = new ConcurrentHashMap<>();

	@Override
	public @NotNull String name() {
		return "blocked_words";
	}

	@Override
	public @NotNull String displayName() {
		return "Blocked Words";
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

			entries.put(
					normalize(raw),
					new BlockedWordsEntry(
							word.getBoolean("cancel", true),
							word.getString("replace", "***"),
							word.getBoolean("detect_in_words", true)
					)
			);
		}
	}

	@Override
	public @NotNull FilterResult process(@NotNull ChatContext ctx) {
		final String input = ctx.plain();
		final String[] parts = input.split(" ", -1);

		boolean modified = false;

		for (int i = 0; i < parts.length; i++) {
			final String original = parts[i];
			final String normalized = normalize(original);

			for (Map.Entry<String, BlockedWordsEntry> e : entries.entrySet()) {
				final String word = e.getKey();
				final BlockedWordsEntry entry = e.getValue();

				final int index = normalized.indexOf(word);

				if (index == -1) {
					continue;
				}

				if (!entry.detectInWords && normalized.length() != word.length()) {
					continue;
				}

				if (entry.cancel) {
					MistMessage.create(ctx.sender())
							.id("modules.filtering.blocked_words.blocked")
							.send();

					return FilterResult.cancelled();
				}

				final int end = index + word.length();

				parts[i] = original.substring(0, index)
						+ entry.replace
						+ original.substring(end);

				modified = true;
				break;
			}
		}

		if (!modified) {
			return FilterResult.pass();
		}

		return FilterResult.modify(String.join(" ", parts));
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

	private record BlockedWordsEntry(
			boolean cancel,
			@NotNull String replace,
			boolean detectInWords
	) {}
}
