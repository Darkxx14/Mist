package com.xyrisdev.mist.extension.filter.rule.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.extension.filter.rule.FilterResult;
import com.xyrisdev.mist.extension.filter.rule.FilterRule;
import com.xyrisdev.mist.util.matcher.SimilarityMatcher;
import com.xyrisdev.mist.util.message.MistMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SimilarityRule implements FilterRule {

	private static final Cache<@NotNull UUID, String> CACHE = Caffeine.newBuilder()
						   .expireAfterWrite(10, TimeUnit.SECONDS)
						   .build();

	private double threshold;

	@Override
	public @NotNull String name() {
		return "similarity";
	}

	@Override
	public @NotNull String displayName() {
		return "Similar Message";
	}

	@Override
	public int priority() {
		return 6;
	}

	@Override
	public void load(@NotNull ConfigurationSection section) {
		this.threshold = section.getDouble("threshold", 0.75);
	}

	@Override
	public @NotNull FilterResult process(@NotNull ChatContext ctx) {
		final UUID id = ctx.sender().getUniqueId();
		final String previous = CACHE.getIfPresent(id);
		final String current = ctx.plain();

		if (previous != null) {
			final double requiredSimilarity = 1.0 - this.threshold;

			if (SimilarityMatcher.similarity(previous, current) >= requiredSimilarity) {
				MistMessage.create(ctx.sender())
						.id("modules.filtering.similarity")
						.send();

				return FilterResult.cancelled();
			}
		}

		CACHE.put(id, current);
		return FilterResult.pass();
	}
}