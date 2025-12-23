package com.xyrisdev.mist.module.filter.config.loader;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.module.filter.config.FilterConfiguration;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import com.xyrisdev.mist.module.filter.rule.impl.*;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class FilterConfigurationLoader {

	private static final List<FilterRule> RULES = List.of(
			new SimilarityRule(),
			new FloodRule(),
			new CapsRule(),
			new CooldownRule(),
			new BlockedWordsRule(),
			new RegexRule()
	);

	@Contract("_ -> new")
	public static @NotNull FilterConfiguration load(CachableConfiguration config) {
		final List<FilterRule> loaded = RULES.stream()
				.map(rule -> {
					ConfigurationSection section = config.getSection(rule.key());
					if (section == null || !rule.enabled(section)) {
						return null;
					}
					rule.load(section);
					return rule;
				})
				.filter(Objects::nonNull)
				.sorted(Comparator.comparingInt(FilterRule::priority))
				.toList();

		return new FilterConfiguration(loaded);
	}
}