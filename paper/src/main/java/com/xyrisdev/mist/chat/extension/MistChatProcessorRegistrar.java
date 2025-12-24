package com.xyrisdev.mist.chat.extension;

import com.xyrisdev.mist.api.chat.extension.ChatProcessorRegistrar;
import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessorStage;
import com.xyrisdev.mist.chat.processor.builder.ChatProcessorBuilder;
import org.jetbrains.annotations.NotNull;

public class MistChatProcessorRegistrar implements ChatProcessorRegistrar {

	private final ChatProcessorBuilder builder;

	public MistChatProcessorRegistrar(@NotNull ChatProcessorBuilder builder) {
		this.builder = builder;
	}

	@Override
	public void register(int order, @NotNull ChatProcessorStage stage) {
		this.builder.add(order, stage);
	}
}
