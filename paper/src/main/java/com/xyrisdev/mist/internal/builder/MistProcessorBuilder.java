package com.xyrisdev.mist.internal.builder;

import com.xyrisdev.mist.api.processor.ChatProcessor;
import com.xyrisdev.mist.api.processor.ChatStage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class MistProcessorBuilder {

	private final List<Entry> entries = new ArrayList<>();

	@Contract("_, _ -> this")
	public MistProcessorBuilder group(
			int groupPriority,
			@NotNull GroupConfigurer configurer
	) {
		configurer.configure(
				(stage, stagePriority) ->
						entries.add(new Entry(groupPriority, stagePriority, stage))
		);
		return this;
	}

	@Contract(" -> new")
	public @NotNull ChatProcessor build() {
		return new ChatProcessor(
				entries.stream()
						.sorted(Comparator
								.comparingInt(Entry::groupPriority)
								.thenComparingInt(Entry::stagePriority))
						.map(Entry::stage)
						.toList()
		);
	}

	private record Entry(
			int groupPriority,
			int stagePriority,
			@NotNull ChatStage stage
	) {}
}
