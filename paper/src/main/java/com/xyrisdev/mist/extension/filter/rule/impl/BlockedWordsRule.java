package com.xyrisdev.mist.extension.filter.rule.impl;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.extension.filter.rule.FilterResult;
import com.xyrisdev.mist.extension.filter.rule.FilterRule;
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
		System.out.println("[Mist DEBUG] load() START");
		matcher.clear();
		entries.clear();

		final ConfigurationSection words = section.getConfigurationSection("words");

		if (words == null) {
			System.out.println("[Mist DEBUG] load() END - no words section");
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
		System.out.println("[Mist DEBUG] load() END - loaded " + entries.size() + " words");
	}

	@Override
	public @NotNull FilterResult process(@NotNull ChatContext context) {
		System.out.println("[Mist DEBUG] process() START - player: " + context.sender().getName());

		final String plain = context.plain();
		System.out.println("[Mist DEBUG] process() - plain message: '" + plain + "'");

		final String normalized = normalize(plain);
		System.out.println("[Mist DEBUG] process() - normalized message: '" + normalized + "'");

		System.out.println("[Mist DEBUG] process() - calling matcher.first()");
		final Optional<AhoCorasickMatcher.Match> matchOpt = matcher.first(normalized);
		System.out.println("[Mist DEBUG] process() - matcher.first() completed");

		if (matchOpt.isEmpty()) {
			System.out.println("[Mist DEBUG] process() END - no match found, returning pass()");
			return FilterResult.pass();
		}

		final AhoCorasickMatcher.Match match = matchOpt.get();
		System.out.println("[Mist DEBUG] process() - match found: '" + match.word() + "' at position " + match.start());

		final Entry entry = entries.get(match.word());

		if (entry == null) {
			System.out.println("[Mist DEBUG] process() END - no entry for match, returning pass()");
			return FilterResult.pass();
		}

		System.out.println("[Mist DEBUG] process() - entry: cancel=" + entry.cancel + ", allowInWords=" + entry.allowInWords);

		if (!entry.allowInWords && insideWord(normalized, match)) {
			System.out.println("[Mist DEBUG] process() END - match inside word, returning pass()");
			return FilterResult.pass();
		}

		if (entry.cancel) {
			System.out.println("[Mist DEBUG] process() - creating MistMessage");
			MistMessage.create(context.sender())
					.id("modules.filtering.blocked_words.blocked")
					.send();
			System.out.println("[Mist DEBUG] process() - MistMessage sent");

			System.out.println("[Mist DEBUG] process() END - returning cancelled()");
			return FilterResult.cancelled();
		}

		System.out.println("[Mist DEBUG] process() - creating replacement string");
		final String modified = plain.replaceAll("(?i)\\b" + escape(match.word()) + "\\b", entry.replace);
		System.out.println("[Mist DEBUG] process() - modified message: '" + modified + "'");

		System.out.println("[Mist DEBUG] process() END - returning modify()");
		return FilterResult.modify(modified);
	}

	private static boolean insideWord(String text, AhoCorasickMatcher.@NotNull Match match) {
		System.out.println("[Mist DEBUG] insideWord() START - text: '" + text + "', word: '" + match.word() + "' at " + match.start());

		final int i = match.start();
		final int i1 = i + match.word().length();

		if (i > 0 && Character.isLetterOrDigit(text.charAt(i - 1))) {
			System.out.println("[Mist DEBUG] insideWord() END - character before is alphanumeric, returning true");
			return true;
		}

		boolean result = i1 < text.length() && Character.isLetterOrDigit(text.charAt(i1));
		System.out.println("[Mist DEBUG] insideWord() END - character after check: " + result);
		return result;
	}

	private static @NotNull String normalize(@NotNull String input) {
		if (input.isEmpty()) {
			return input;
		}

		System.out.println("[Mist DEBUG] normalize() START - input: '" + input + "'");

		String s = Normalizer.normalize(input, Normalizer.Form.NFKC);
		System.out.println("[Mist DEBUG] After NFKC normalization: '" + s + "'");

		s = s.toLowerCase();
		System.out.println("[Mist DEBUG] After lowercase: '" + s + "'");

		s = s.replaceAll("\\p{M}+", "");
		System.out.println("[Mist DEBUG] After diacritic removal: '" + s + "'");

		s = LeetMap.map(s);
		System.out.println("[Mist DEBUG] After LeetMap mapping: '" + s + "'");

		s = s.replaceAll("[^a-z0-9]+", " ");
		System.out.println("[Mist DEBUG] After non-alphanumeric removal: '" + s + "'");

		String result = s.trim().replaceAll("\\s+", " ");
		System.out.println("[Mist DEBUG] normalize() END - result: '" + result + "'");

		return result;
	}

	@Contract(pure = true)
	private static @NotNull String escape(@NotNull String s) {
		System.out.println("[Mist DEBUG] escape() START - input: '" + s + "'");
		String result = s.replaceAll("([\\\\.*+?\\[^\\]$(){}=!<>|:-])", "\\\\$1");
		System.out.println("[Mist DEBUG] escape() END - result: '" + result + "'");
		return result;
	}

	private record Entry(boolean cancel, @NotNull String replace, boolean allowInWords) {}
}