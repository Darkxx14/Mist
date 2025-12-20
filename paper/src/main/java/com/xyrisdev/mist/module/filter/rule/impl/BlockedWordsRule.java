package com.xyrisdev.mist.module.filter.rule.impl;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.module.filter.rule.FilterResult;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import com.xyrisdev.mist.util.regex.RegexGenerator;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BlockedWordsRule implements FilterRule {

	private final List<Entry> entries = new ArrayList<>();

	@Override
	public @NotNull String key() {
		return "blocked_words";
	}

	@Override
	public void load(@NotNull ConfigurationSection section) {
		entries.clear();

		final ConfigurationSection words = section.getConfigurationSection("words");

		if (words == null) {
			return;
		}

		for (String word : words.getKeys(false)) {
			final ConfigurationSection w = words.getConfigurationSection(word);

			if (w == null) {
				continue;
			}

			final RegexGenerator.Level level = RegexGenerator.Level.valueOf(
					w.getString("type", "BASIC")
							.toUpperCase()
							.replace(" ", "_")
			);

			entries.add(new Entry(
					RegexGenerator.generate(word, level),
					w.getBoolean("cancel_message", true),
					w.getString("replace_with", "*")
			));
		}
	}

	@Override
	public @NotNull FilterResult process(@NotNull ChatContext context) {
		String msg = context.plain();

		for (Entry entry : entries) {
			if (!entry.pattern.matcher(msg).find()) {
				continue;
			}

			if (entry.cancel) {
				return FilterResult.cancelled();
			}

			msg = entry.pattern.matcher(msg).replaceAll(entry.replace);
		}

		return msg.equals(context.plain())
				? FilterResult.pass()
				: FilterResult.modify(msg);
	}

	private record Entry(@NotNull Pattern pattern, boolean cancel, String replace) { }
}
