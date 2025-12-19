package com.xyrisdev.mist.module.filter.rule.factory;

import com.xyrisdev.mist.module.filter.rule.FilterRule;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FilterRuleFactory {

	@Nullable
	FilterRule load(@NotNull ConfigurationSection section);
}
