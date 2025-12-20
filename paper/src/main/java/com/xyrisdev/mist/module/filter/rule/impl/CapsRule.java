package com.xyrisdev.mist.module.filter.rule.impl;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.module.filter.rule.FilterResult;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import org.jetbrains.annotations.NotNull;
import com.xyrisdev.mist.module.filter.rule.factory.FilterRuleFactory;

public class CapsRule implements FilterRule {

	public enum Type {
		RATIO, DEFINED
	}

	public static final FilterRuleFactory FACTORY = section ->
			new CapsRule(
					section.getBoolean("enabled", false),
					Type.valueOf(section.getString("type", "RATIO").toUpperCase()),
					section.getInt("max_caps", 5),
					section.getDouble("ratio", 0.7)
			);

	private final boolean enabled;
	private final Type type;
	private final int maxCaps;
	private final double ratio;

	private CapsRule(boolean enabled, Type type, int maxCaps, double ratio) {
		this.enabled = enabled;
		this.type = type;
		this.maxCaps = maxCaps;
		this.ratio = ratio;
	}

	@Override
	public boolean enabled() {
		return enabled;
	}

	@Override
	public @NotNull FilterResult process(@NotNull ChatContext context) {
		final String msg = context.plain();
		int caps = 0;

		for (char c : msg.toCharArray()) {
			if (Character.isUpperCase(c)) caps++;
		}

		if (type == Type.DEFINED && caps > maxCaps) {
			return FilterResult.cancelled();
		}

		if (type == Type.RATIO && !msg.isEmpty()) {
			if ((double) caps / msg.length() > ratio) {
				return FilterResult.cancelled();
			}
		}

		return FilterResult.pass();
	}
}
