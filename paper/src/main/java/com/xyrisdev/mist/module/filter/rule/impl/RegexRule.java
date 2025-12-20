package com.xyrisdev.mist.module.filter.rule.impl;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.module.filter.rule.FilterResult;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import com.xyrisdev.mist.module.filter.rule.factory.FilterRuleFactory;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegexRule implements FilterRule {

	public static final FilterRuleFactory FACTORY = section -> {
		if (!section.getBoolean("enabled", false)) {
			return new RegexRule(false, List.of());
		}

		final List<Entry> entries = new ArrayList<>();
		final ConfigurationSection patterns = section.getConfigurationSection("patterns");

		if (patterns != null) {
			for (String raw : patterns.getKeys(false)) {
				final ConfigurationSection p = patterns.getConfigurationSection(raw);

				if (p == null) {
					continue;
				}

				try {
					entries.add(new Entry(
							Pattern.compile(raw),
							p.getBoolean("cancel_message", true),
							p.getString("replace_with", "***")
					));
				} catch (Exception ignored) {}
			}
		}

		return new RegexRule(true, entries);
	};

	private record Entry(Pattern pattern, boolean cancel, String replace) {}

	private final boolean enabled;
	private final List<Entry> entries;

	private RegexRule(boolean enabled, List<Entry> entries) {
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
