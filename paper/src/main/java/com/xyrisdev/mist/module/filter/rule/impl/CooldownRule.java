package com.xyrisdev.mist.module.filter.rule.impl;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.module.filter.rule.FilterResult;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import com.xyrisdev.mist.module.filter.rule.factory.FilterRuleFactory;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public final class CooldownRule implements FilterRule {

	private static final Key COOLDOWN_KEY = Key.key("mist", "chat_cooldown");

	public static final FilterRuleFactory FACTORY = section ->
			new CooldownRule(
					section.getBoolean("enabled", false),
					section.getInt("cooldown_seconds", 3)
			);

	private final boolean enabled;
	private final int seconds;

	private CooldownRule(boolean enabled, int seconds) {
		this.enabled = enabled;
		this.seconds = seconds;
	}

	@Override
	public boolean enabled() {
		return enabled;
	}

	@Override
	public @NotNull FilterResult process(@NotNull ChatContext context) {
		final long now = System.currentTimeMillis();
		final Long last = context.data(COOLDOWN_KEY, Long.class);

		if (last != null && (now - last) < seconds * 1000L) {
			return FilterResult.cancelled();
		}

		context.data(COOLDOWN_KEY, now);
		return FilterResult.pass();
	}
}
