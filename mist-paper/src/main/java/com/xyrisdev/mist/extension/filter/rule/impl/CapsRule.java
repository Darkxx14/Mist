package com.xyrisdev.mist.extension.filter.rule.impl;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import com.xyrisdev.mist.extension.filter.rule.FilterResult;
import com.xyrisdev.mist.extension.filter.rule.FilterRule;
import com.xyrisdev.mist.util.message.MistMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class CapsRule implements FilterRule {

	private CapsRuleType type = CapsRuleType.RATIO;
	private int maxCaps;
	private double ratio;

	@Override
	public @NotNull String name() {
		return "caps";
	}

	@Override
	public @NotNull String displayName() {
		return "Excessive Caps";
	}

	@Override
	public int priority() {
		return 4;
	}

	@Override
	public void load(@NotNull ConfigurationSection section) {
		this.type = CapsRuleType.valueOf(section.getString("type", "RATIO").toUpperCase());
		this.maxCaps = section.getInt("max_caps", 5);
		this.ratio = section.getDouble("ratio", 0.7);
	}

	@Override
	public @NotNull FilterResult process(@NotNull ChatContext ctx) {
		final String msg = ctx.plain();
		int caps = 0;

		for (char c : msg.toCharArray()) {
			if (Character.isUpperCase(c)) caps++;
		}

		if (this.type == CapsRuleType.DEFINED && caps > this.maxCaps) {
			MistMessage.create(ctx.sender())
					.id("modules.filtering.caps")
					.placeholder("max", String.valueOf(this.maxCaps))
					.send();

			return FilterResult.cancelled();
		}

		if (this.type == CapsRuleType.RATIO && !msg.isEmpty() && (double) caps / msg.length() > this.ratio) {
			MistMessage.create(ctx.sender())
					.id("modules.filtering.caps")
					.placeholder("max", String.valueOf(this.maxCaps))
					.send();

			return FilterResult.cancelled();
		}

		return FilterResult.pass();
	}

	public enum CapsRuleType {
		RATIO, DEFINED
	}
}
