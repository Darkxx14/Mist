package com.xyrisdev.mist.module.filter.rule.impl;

import com.xyrisdev.mist.api.context.ChatContext;
import com.xyrisdev.mist.module.filter.rule.FilterResult;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class CapsRule implements FilterRule {

	private CapsRuleType type = CapsRuleType.RATIO;
	private int maxCaps;
	private double ratio;

	@Override
	public @NotNull String key() {
		return "caps";
	}

	@Override
	public void load(@NotNull ConfigurationSection section) {
		type = CapsRuleType.valueOf(section.getString("type", "RATIO").toUpperCase());
		maxCaps = section.getInt("max_caps", 5);
		ratio = section.getDouble("ratio", 0.7);
	}

	@Override
	public @NotNull FilterResult process(@NotNull ChatContext context) {
		final String msg = context.plain();
		int caps = 0;

		for (char c : msg.toCharArray()) {
			if (Character.isUpperCase(c)) caps++;
		}

		if (type == CapsRuleType.DEFINED && caps > maxCaps) {
			return FilterResult.cancelled();
		}

		if (type == CapsRuleType.RATIO && !msg.isEmpty()) {
			if ((double) caps / msg.length() > ratio) {
				return FilterResult.cancelled();
			}
		}

		return FilterResult.pass();
	}

	public enum CapsRuleType {
		RATIO, DEFINED
	}
}
