package com.xyrisdev.mist.module.filter.rule.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.module.filter.rule.FilterResult;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import com.xyrisdev.mist.util.SimilarityUtil;
import com.xyrisdev.mist.util.message.MistMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SimilarityRule implements FilterRule {

	private static final Cache<UUID, String> CACHE = Caffeine.newBuilder()
			.expireAfterWrite(10, TimeUnit.SECONDS)
			.build();

	private double threshold;

	@Override
	public @NotNull String key() {
		return "similarity";
	}

	@Override
	public void load(@NotNull ConfigurationSection section) {
		threshold = section.getDouble("threshold", 0.75);
	}

	@Override
	public @NotNull FilterResult process(@NotNull ChatContext ctx) {
		final UUID playerId = ctx.player().getUniqueId();
		final String previous = CACHE.getIfPresent(playerId);
		final String current = ctx.plain();

		if (previous != null && SimilarityUtil.similarity(previous, current) >= threshold) {
			MistMessage.create(ctx.player())
					.id("modules.filtering.similarity")
					.send();

			return FilterResult.cancelled();
		}

		CACHE.put(playerId, current);
		return FilterResult.pass();
	}
}