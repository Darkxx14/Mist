package com.xyrisdev.mist.module.filter.rule.impl;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.module.filter.rule.FilterResult;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import com.xyrisdev.mist.util.matcher.AhoCorasickMatcher;
import com.xyrisdev.mist.util.matcher.LeetMap;
import com.xyrisdev.mist.util.message.MistMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.Normalizer;
import java.util.*;

public class BlockedWordsRule implements FilterRule {

	private final AhoCorasickMatcher matcher = new AhoCorasickMatcher();
	private final Map<String, Entry> entries = new HashMap<>();

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
		matcher.clear();
		entries.clear();

		final ConfigurationSection words = section.getConfigurationSection("words");

		if (words == null) {
			return;
		}

		for (String raw : words.getKeys(false)) {
			final ConfigurationSection wordsSection = words.getConfigurationSection(raw);

			if (wordsSection == null) {
				continue;
			}

			final String normalized = normalize(raw);

			final boolean cancel = wordsSection.getBoolean("cancel_message", true);
			final String replace = wordsSection.getString("replace_with", "***");
			final boolean allowInWords = wordsSection.getBoolean("allow_in_words", false);

			matcher.add(normalized);
			entries.put(normalized, new Entry(cancel, replace, allowInWords));
		}

		matcher.build();
	}

	@Override
	public @NotNull FilterResult process(@NotNull ChatContext context) {
		final String normalized = normalize(context.plain());

		final Optional<AhoCorasickMatcher. Match> matchOpt = matcher.first(normalized);

		if (matchOpt.isEmpty()) {
			return FilterResult.pass();
		}

		final AhoCorasickMatcher.Match match = matchOpt.get();
		final Entry entry = entries.get(match.word());

		if (entry == null) {
			return FilterResult.pass();
		}

		if (!entry.allowInWords && insideWord(normalized, match)) {
			return FilterResult.pass();
		}

		if (entry.cancel) {
			MistMessage.create(context.player())
					.id("modules.filtering.blocked_words.blocked")
					.send();

			return FilterResult.cancelled();
		}

		final String modified = context.plain().replaceAll("(?i)\\b" + escape(match.word()) + "\\b", entry.replace);

		return FilterResult.modify(modified);
	}

	private static boolean insideWord(String text, AhoCorasickMatcher.@NotNull Match match) {
		final int i = match.start();
		final int i1 = i + match.word().length();

		if (i > 0 && Character.isLetterOrDigit(text.charAt(i - 1))) {
			return true;
		}

		return i1 < text.length() && Character.isLetterOrDigit(text.charAt(i1));
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

	@Contract(pure = true)
	private static @NotNull String escape(@NotNull String s) {
		return s.replaceAll("([\\\\.*+?\\[^\\]$(){}=!<>|:-])", "\\\\$1");
	}

	private record Entry(boolean cancel, @NotNull String replace, boolean allowInWords) {}
}
