package com.xyrisdev.mist.extension.filter.config;

import com.xyrisdev.mist.extension.filter.rule.FilterRule;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record FilterConfiguration(
		@NotNull List<FilterRule> rules
) {}
