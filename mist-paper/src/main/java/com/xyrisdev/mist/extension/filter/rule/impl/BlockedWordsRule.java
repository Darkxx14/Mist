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
	private int minWordLength = Integer.MAX_VALUE;

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
		this.entries.clear();
		this.minWordLength = Integer.MAX_VALUE;

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
			this.minWordLength = Math.min(this.minWordLength, normalized.length());

			this.entries.put(
					normalized,
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

			if (original.length() < this.minWordLength) {
				continue;
			}

			final String normalized = normalize(original);

			for (Map.Entry<String, BlockedWordsEntry> e : this.entries.entrySet()) {
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
						+ original.substring(Math.min(end, original.length()));

				modified = true;
				break;
			}
		}

		return modified
				? FilterResult.modify(String.join(" ", parts))
				: FilterResult.pass();
	}

	private static @NotNull String normalize(@NotNull String input) {
		if (input.isEmpty()) {
			return input;
		}

		final String decomposed = Normalizer.normalize(input, Normalizer.Form.NFKC);
		final StringBuilder out = new StringBuilder(decomposed.length());

		boolean lastWasSpace = true;

		for (int i = 0; i < decomposed.length(); i++) {
			char c = Character.toLowerCase(decomposed.charAt(i));

			if (Character.getType(c) == Character.NON_SPACING_MARK) {
				continue;
			}

			c = LeetMap.map(c);

			if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
				out.append(c);
				lastWasSpace = false;
			} else if (!lastWasSpace) {
				out.append(' ');
				lastWasSpace = true;
			}
		}

		final int len = out.length();

		if (len > 0 && out.charAt(len - 1) == ' ') {
			out.setLength(len - 1);
		}

		return out.toString();
	}

	private record BlockedWordsEntry(
			boolean cancel,
			@NotNull String replace,
			boolean detectInWords
	) {}
}
