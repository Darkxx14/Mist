package com.xyrisdev.mist.extension.filter.rule;

import com.xyrisdev.mist.api.chat.context.ChatContext;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public interface FilterRule {

	@NotNull String key();

	int priority();

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	default boolean enabled(@NotNull ConfigurationSection section) {
		return section.getBoolean("enabled", false);
	}

	void load(@NotNull ConfigurationSection section);

	@NotNull
	FilterResult process(@NotNull ChatContext ctx);
}
