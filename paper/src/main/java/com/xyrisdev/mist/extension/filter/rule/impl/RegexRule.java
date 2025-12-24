package com.xyrisdev.mist.extension.filter.rule.impl;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.extension.filter.rule.FilterResult;
import com.xyrisdev.mist.extension.filter.rule.FilterRule;
import com.xyrisdev.mist.util.message.MistMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
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

		for (String id : patterns.getKeys(false)) {
			final ConfigurationSection rule = patterns.getConfigurationSection(id);

			if (rule == null) {
				continue;
			}

			final String regex = rule.getString("pattern");

			if (regex == null || regex.isBlank()) {
				continue;
			}

			try {
				entries.add(new Entry(
						id,
						Pattern.compile(regex),
						rule.getBoolean("cancel", false),
						rule.getString("replace", "***")
				));
			} catch (Exception ex) {
				System.out.println(
						"[RegexRule] Invalid regex '" + id + "': " + ex.getMessage()
				);
			}
		}
	}

	@Override
	public @NotNull FilterResult process(@NotNull ChatContext ctx) {
		final String original = ctx.plain();
		String msg = original;

		for (Entry entry : entries) {
			final Matcher matcher = entry.pattern.matcher(msg);

			if (!matcher.find()) {
				continue;
			}

			if (entry.cancel) {
				MistMessage.create(ctx.sender())
						.id("modules.filtering.regex.blocked")
						.placeholder("rule", entry.id)
						.send();

				return FilterResult.cancelled();
			}

			msg = matcher.replaceAll(entry.replace);
		}

		return msg.equals(original)
				? FilterResult.pass()
				: FilterResult.modify(msg);
	}

	private record Entry(
			@NotNull String id,
			@NotNull Pattern pattern,
			boolean cancel,
			@NotNull String replace
	) {}
}
