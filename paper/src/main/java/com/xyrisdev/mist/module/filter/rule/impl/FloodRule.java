package com.xyrisdev.mist.module.filter.rule.impl;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.module.filter.rule.FilterResult;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import com.xyrisdev.mist.module.filter.rule.factory.FilterRuleFactory;
import org.jetbrains.annotations.NotNull;

public final class FloodRule implements FilterRule {

	public static final FilterRuleFactory FACTORY = section ->
			new FloodRule(
					section.getBoolean("enabled", false),
					section.getInt("maximum_length", 90),
					section.getInt("maximum_repeated_chars", 5)
			);

	private final boolean enabled;
	private final int maxLength;
	private final int maxRepeats;

	private FloodRule(boolean enabled, int maxLength, int maxRepeats) {
		this.enabled = enabled;
		this.maxLength = maxLength;
		this.maxRepeats = maxRepeats;
	}

	@Override
	public boolean enabled() {
		return enabled;
	}

	@Override
	public @NotNull FilterResult process(@NotNull ChatContext context) {
		final String msg = context.plain();

		if (msg.length() > maxLength) {
			return FilterResult.cancelled();
		}

		int streak = 1;

		for (int i = 1; i < msg.length(); i++) {
			if (msg.charAt(i) == msg.charAt(i - 1)) {
				if (++streak > maxRepeats) {
					return FilterResult.cancelled();
				}
			} else {
				streak = 1;
			}
		}

		return FilterResult.pass();
	}
}
