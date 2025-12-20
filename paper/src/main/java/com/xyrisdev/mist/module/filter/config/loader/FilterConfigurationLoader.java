package com.xyrisdev.mist.module.filter.config.loader;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.module.filter.config.FilterConfiguration;
import com.xyrisdev.mist.module.filter.rule.FilterRule;
import com.xyrisdev.mist.module.filter.rule.factory.FilterRuleFactory;
import com.xyrisdev.mist.module.filter.rule.impl.RegexRule;
import com.xyrisdev.mist.module.filter.rule.impl.BlockedWordsRule;
import com.xyrisdev.mist.module.filter.rule.impl.CapsRule;
import com.xyrisdev.mist.module.filter.rule.impl.CooldownRule;
import com.xyrisdev.mist.module.filter.rule.impl.FloodRule;
import com.xyrisdev.mist.module.filter.rule.impl.SimilarityRule;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilterConfigurationLoader {

	private static final Map<String, FilterRuleFactory> RULES = Map.of(
			"similarity", SimilarityRule.FACTORY,
			"flood", FloodRule.FACTORY,
			"caps", CapsRule.FACTORY,
			"cooldown", CooldownRule.FACTORY,
			"blocked_words", BlockedWordsRule.FACTORY,
			"regex", RegexRule.FACTORY
	);

	public static @NotNull FilterConfiguration load(@NotNull CachableConfiguration config) {
		final List<FilterRule> rules = new ArrayList<>();

		for (Map.Entry<String, FilterRuleFactory> entry : RULES.entrySet()) {
			final ConfigurationSection section = config.getSection(entry.getKey());

			if (section == null) {
				continue;
			}

			final FilterRule rule = entry.getValue().load(section);

			if (rule != null) {
				rules.add(rule);
			}
		}

		return new FilterConfiguration(rules);
	}
}