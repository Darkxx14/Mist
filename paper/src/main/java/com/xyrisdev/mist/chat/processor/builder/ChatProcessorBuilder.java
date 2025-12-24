package com.xyrisdev.mist.chat.processor.builder;

import com.xyrisdev.mist.api.chat.processor.ChatProcessor;
import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessorStage;
import com.xyrisdev.mist.chat.processor.DefaultChatProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChatProcessorBuilder {

	private final List<Entry> entries = new ArrayList<>();

	public void add(int order, @NotNull ChatProcessorStage stage) {
		entries.add(new Entry(order, stage));
	}

	public ChatProcessor build() {
		return new DefaultChatProcessor(
				entries.stream()
						.sorted(Comparator.comparingInt(Entry::order))
						.map(Entry::stage)
						.toList()
		);
	}

	private record Entry(
			int order,
			@NotNull ChatProcessorStage stage)
	{}
}
