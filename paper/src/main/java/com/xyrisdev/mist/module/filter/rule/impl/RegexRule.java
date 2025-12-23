package com.xyrisdev.mist.module.filter.rule.impl;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.module.filter.rule.FilterResult;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import com.xyrisdev.mist.util.message.MistMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegexRule implements FilterRule {

	private final List<Entry> entries = new ArrayList<>();

	@Override
	public @NotNull String key() {
		return "regex";
	}

	@Override
	public int priority() {
		return 2;
	}

	@Override
	public void load(@NotNull ConfigurationSection section) {
		entries.clear();

		final ConfigurationSection patterns = section.getConfigurationSection("patterns");

		if (patterns == null) {
			return;
		}

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

	@Override
	public @NotNull FilterResult process(@NotNull ChatContext context) {
		String msg = context.plain();

		for (Entry e : entries) {
			if (!e.pattern.matcher(msg).find()) {
				continue;
			}

			if (e.cancel) {
				MistMessage.create(context.player())
						.id("modules.filtering.regex.blocked")
						.send();

				return FilterResult.cancelled();
			}

			msg = e.pattern.matcher(msg).replaceAll(e.replace);
		}

		return msg.equals(context.plain())
				? FilterResult.pass()
				: FilterResult.modify(msg);
	}

	private record Entry(@NotNull Pattern pattern, boolean cancel, String replace) {}
}
