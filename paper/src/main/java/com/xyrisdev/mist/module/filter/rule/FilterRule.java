package com.xyrisdev.mist.module.filter.rule;

import com.xyrisdev.mist.api.context.ChatContext;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public interface FilterRule {

	@NotNull String key();

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	default boolean enabled(@NotNull ConfigurationSection section) {
		return section.getBoolean("enabled", false);
	}

	void load(@NotNull ConfigurationSection section);

	@NotNull
	FilterResult process(@NotNull ChatContext context);
}
