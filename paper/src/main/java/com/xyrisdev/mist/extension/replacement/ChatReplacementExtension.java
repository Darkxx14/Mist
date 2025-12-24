package com.xyrisdev.mist.extension.replacement;

import com.xyrisdev.mist.api.chat.extension.meta.ExtensionMetadata;
import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessStage;
import com.xyrisdev.mist.extension.replacement.config.ReplacementsConfiguration;
import com.xyrisdev.mist.extension.replacement.stage.ReplacementStage;
import org.jetbrains.annotations.NotNull;

public class ChatReplacementExtension implements ExtensionMetadata {

	private final ReplacementsConfiguration config;

	public ChatReplacementExtension(@NotNull ReplacementsConfiguration config) {
		this.config = config;
	}

	@Override
	public @NotNull String name() {
		return "chat_replacement";
	}

	@Override
	public @NotNull Class<? extends ChatProcessStage> stage() {
		return ReplacementStage.class;
	}

	@Override
	public Object configuration() {
		return this.config;
	}
}