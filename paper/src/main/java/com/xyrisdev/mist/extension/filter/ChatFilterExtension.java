package com.xyrisdev.mist.extension.filter;

import com.xyrisdev.mist.api.chat.extension.meta.ExtensionMetadata;
import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessStage;
import com.xyrisdev.mist.extension.filter.config.FilterConfiguration;
import com.xyrisdev.mist.extension.filter.stage.FilterStage;
import org.jetbrains.annotations.NotNull;

public class ChatFilterExtension implements ExtensionMetadata {

	private final FilterConfiguration config;

	public ChatFilterExtension(@NotNull FilterConfiguration config) {
		this.config = config;
	}

	@Override
	public @NotNull String name() {
		return "chat_filter";
	}

	@Override
	public @NotNull Class<? extends ChatProcessStage> stage() {
		return FilterStage.class;
	}

	@Override
	public Object configuration() {
		return this.config;
	}
}
