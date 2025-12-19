package com.xyrisdev.mist.module.filter.rule.impl;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.module.filter.rule.FilterResult;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import com.xyrisdev.mist.module.filter.rule.factory.FilterRuleFactory;
import com.xyrisdev.mist.util.regex.RegexGenerator;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class BlockedWordsRule implements FilterRule {

	private record Entry(Pattern pattern, boolean cancel, String replace) {}

	public static final FilterRuleFactory FACTORY = section -> {
		if (!section.getBoolean("enabled", false)) {
			return new BlockedWordsRule(false, List.of());
		}

		final List<Entry> entries = new ArrayList<>();
		final ConfigurationSection words = section.getConfigurationSection("words");

		if (words != null) {
			for (String key : words.getKeys(false)) {
				final ConfigurationSection w = words.getConfigurationSection(key);

				if (w == null) {
					continue;
				}

				final RegexGenerator.Level level =
						RegexGenerator.Level.valueOf(
								w.getString("type", "BASIC")
										.toUpperCase()
										.replace(" ", "_")
						);

				entries.add(new Entry(
						RegexGenerator.generate(key, level),
						w.getBoolean("cancel_message", true),
						w.getString("replace_with", "*")
				));
			}
		}

		return new BlockedWordsRule(true, entries);
	};

	private final boolean enabled;
	private final List<Entry> entries;

	private BlockedWordsRule(boolean enabled, List<Entry> entries) {
		this.enabled = enabled;
		this.entries = entries;
	}

	@Override
	public boolean enabled() {
		return enabled;
	}

	@Override
	public @NotNull FilterResult process(@NotNull ChatContext context) {
		String msg = context.plain();

		for (Entry e : entries) {
			if (!e.pattern.matcher(msg).find()) {
				continue;
			}

			if (e.cancel) {
				return FilterResult.cancelled();
			}

			msg = e.pattern.matcher(msg).replaceAll(e.replace);
		}

		return msg.equals(context.plain())
				? FilterResult.pass()
				: FilterResult.modify(msg);
	}
}
