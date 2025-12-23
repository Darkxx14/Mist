package com.xyrisdev.mist.module.filter.rule.impl;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.module.filter.rule.FilterResult;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import com.xyrisdev.mist.util.message.MistMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class FloodRule implements FilterRule {

	private int maxLength;
	private int maxRepeats;

	@Override
	public @NotNull String key() {
		return "flood";
	}

	@Override
	public int priority() {
		return 3;
	}

	@Override
	public void load(@NotNull ConfigurationSection section) {
		maxLength = section.getInt("maximum_length", 100);
		maxRepeats = section.getInt("maximum_repeated_chars", 5);
	}

	@Override
	public @NotNull FilterResult process(@NotNull ChatContext context) {
		final String msg = context.plain();

		if (msg.length() > maxLength) {
			MistMessage.create(context.player())
					.id("modules.filtering.flood.length")
					.placeholder("max", String.valueOf(maxLength))
					.send();

			return FilterResult.cancelled();
		}

		int streak = 1;

		for (int i = 1; i < msg.length(); i++) {
			if (msg.charAt(i) == msg.charAt(i - 1)) {
				if (++streak > maxRepeats) {
					MistMessage.create(context.player())
							.id("modules.filtering.flood.repeats")
							.placeholder("max", String.valueOf(maxRepeats))
							.send();

					return FilterResult.cancelled();
				}
			} else {
				streak = 1;
			}
		}

		return FilterResult.pass();
	}
}
