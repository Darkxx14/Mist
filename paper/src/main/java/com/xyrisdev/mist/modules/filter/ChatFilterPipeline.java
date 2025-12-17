package com.xyrisdev.mist.modules.filter;

import com.xyrisdev.mist.modules.filter.object.FilterDecision;
import com.xyrisdev.mist.modules.filter.object.FilterResult;
import com.xyrisdev.mist.modules.filter.object.FilterSubmodule;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public final class ChatFilterPipeline {

	private final List<FilterSubmodule> modules;

	public ChatFilterPipeline(@NotNull List<FilterSubmodule> modules) {
		this.modules = modules.stream()
				.filter(FilterSubmodule::enabled)
				.sorted(Comparator.comparingInt(FilterSubmodule::priority))
				.toList();
	}

	public @NotNull FilterResult process(UUID id, @NotNull String message) {
		String current = message;

		for (FilterSubmodule module : modules) {
			final FilterResult result = module.handle(id, current);

			if (result.decision() == FilterDecision.BLOCK) {
				return result;
			}

			if (result.decision() == FilterDecision.MODIFY) {
				current = result.modifiedMessage();
			}
		}

		return FilterResult.pass();
	}
}
