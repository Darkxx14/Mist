package com.xyrisdev.mist.modules.filter.submodules;

import com.xyrisdev.mist.modules.filter.config.RegexFilterConfig;
import com.xyrisdev.mist.modules.filter.object.FilterResult;
import com.xyrisdev.mist.modules.filter.object.FilterSubmodule;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public final class RegexFilterSubmodule implements FilterSubmodule {

	private final RegexFilterConfig config;
	private final List<Pattern> compiled;

	public RegexFilterSubmodule(@NotNull RegexFilterConfig config) {
		this.config = config;
		this.compiled = config.patterns().stream()
				.map(Pattern::compile)
				.toList();
	}

	@Override
	public @NotNull FilterResult handle(@NotNull UUID id, @NotNull String message) {
		for (Pattern pattern : compiled) {
			if (pattern.matcher(message).find()) {
				return act(message);
			}
		}
		return FilterResult.pass();
	}

	private FilterResult act(String message) {
		if (config.cancelMessage()) {
			return FilterResult.block();
		}

		String replaced = message.replaceAll("\\.", String.valueOf(config.replaceWith()));
		return FilterResult.modify(replaced);
	}

	@Override
	public int priority() {
		return 5;
	}

	@Override
	public boolean enabled() {
		return config.enable();
	}
}
