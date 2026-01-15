package com.xyrisdev.mist.extension.filter.rule.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.extension.filter.rule.FilterResult;
import com.xyrisdev.mist.extension.filter.rule.FilterRule;
import com.xyrisdev.mist.util.message.MistMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownRule implements FilterRule {

	private static final Cache<@NotNull UUID, Long> CACHE = Caffeine.newBuilder()
						  .expireAfterWrite(5, TimeUnit.MINUTES)
						  .build();

	private int seconds;

	@Override
	public @NotNull String name() {
		return "cooldown";
	}

	@Override
	public @NotNull String displayName() {
		return "Cooldown";
	}

	@Override
	public int priority() {
		return 5;
	}

	@Override
	public void load(@NotNull ConfigurationSection section) {
		this.seconds = section.getInt("cooldown_seconds", 3);
	}

	@Override
	public @NotNull FilterResult process(@NotNull ChatContext ctx) {
		final long now = System.currentTimeMillis();
		final UUID id = ctx.sender().getUniqueId();
		final Long last = CACHE.getIfPresent(id);

		if (last != null && (now - last) < this.seconds * 1000L) {
			final long remaining = this.seconds - ((now - last) / 1000L);

			MistMessage.create(ctx.sender())
					.id("modules.filtering.cooldown")
					.placeholder("remaining", String.valueOf(remaining))
					.send();

			return FilterResult.cancelled();
		}

		CACHE.put(id, now);

		return FilterResult.pass();
	}
}