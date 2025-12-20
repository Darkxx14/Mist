package com.xyrisdev.mist.module.filter.rule.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.module.filter.rule.FilterResult;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import com.xyrisdev.mist.util.message.MistMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownRule implements FilterRule {

	private static final Cache<UUID, Long> CACHE = Caffeine.newBuilder()
			.expireAfterWrite(5, TimeUnit.MINUTES)
			.build();

	private int seconds;

	@Override
	public @NotNull String key() {
		return "cooldown";
	}

	@Override
	public void load(@NotNull ConfigurationSection section) {
		seconds = section.getInt("cooldown_seconds", 3);
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