package com.xyrisdev.mist.module.filter.rule.impl;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.module.filter.rule.FilterResult;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import com.xyrisdev.mist.module.filter.rule.factory.FilterRuleFactory;
import com.xyrisdev.mist.util.SimilarityUtil;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public final class SimilarityRule implements FilterRule {

	private static final Key LAST_MESSAGE_KEY = Key.key("mist", "last_message");

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
	public @NotNull FilterResult process(@NotNull ChatContext context) {
		final String previous = context.data(LAST_MESSAGE_KEY, String.class);
		final String current = context.plain();

		if (previous != null && SimilarityUtil.similarity(previous, current) >= threshold) {
			return FilterResult.cancelled();
		}

		context.data(LAST_MESSAGE_KEY, current);
		return FilterResult.pass();
	}
}
