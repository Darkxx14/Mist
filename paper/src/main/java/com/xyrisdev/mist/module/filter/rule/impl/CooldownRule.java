package com.xyrisdev.mist.module.filter.rule.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.module.filter.rule.FilterResult;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import com.xyrisdev.mist.module.filter.rule.factory.FilterRuleFactory;
import com.xyrisdev.mist.util.message.MistMessage;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownRule implements FilterRule {

	private static final Cache<UUID, Long> CACHE = Caffeine.newBuilder()
			.expireAfterWrite(5, TimeUnit.MINUTES)
			.build();

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
	public @NotNull FilterResult process(@NotNull ChatContext ctx) {
		final long now = System.currentTimeMillis();
		final UUID playerId = ctx.player().getUniqueId();
		final Long last = CACHE.getIfPresent(playerId);

		if (last != null && (now - last) < seconds * 1000L) {
			final long remaining = seconds - ((now - last) / 1000L);

			MistMessage.create(ctx.player())
					.id("modules.filtering.cooldown")
					.placeholder("remaining", String.valueOf(remaining))
					.send();

			return FilterResult.cancelled();
		}

		CACHE.put(playerId, now);
		return FilterResult.pass();
	}
}