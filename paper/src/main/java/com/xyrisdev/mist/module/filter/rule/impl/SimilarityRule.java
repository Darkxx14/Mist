package com.xyrisdev.mist.module.filter.rule.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.module.filter.rule.FilterResult;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import com.xyrisdev.mist.module.filter.rule.factory.FilterRuleFactory;
import com.xyrisdev.mist.util.SimilarityUtil;
import com.xyrisdev.mist.util.message.MistMessage;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SimilarityRule implements FilterRule {

	private static final Cache<UUID, String> CACHE = Caffeine.newBuilder()
			.expireAfterWrite(10, TimeUnit.SECONDS)
			.build();

	public static final FilterRuleFactory FACTORY = section ->
			new SimilarityRule(
					section.getBoolean("enabled", false),
					section.getInt("threshold", 75)
			);

	private final boolean enabled;
	private final int threshold;

	private SimilarityRule(boolean enabled, int threshold) {
		this.enabled = enabled;
		this.threshold = threshold;
	}

	@Override
	public boolean enabled() {
		return enabled;
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