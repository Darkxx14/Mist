package com.xyrisdev.mist.module.filter.config;

import com.xyrisdev.mist.module.filter.rule.FilterRule;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record FilterConfiguration(
		@NotNull List<FilterRule> rules
) {}
