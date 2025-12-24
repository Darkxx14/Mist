package com.xyrisdev.mist.extension.format;

import com.xyrisdev.mist.api.chat.extension.meta.ExtensionMetadata;
import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessStage;
import com.xyrisdev.mist.extension.format.config.FormatConfiguration;
import com.xyrisdev.mist.extension.format.stage.FormatStage;
import org.jetbrains.annotations.NotNull;

public class ChatFormatExtension implements ExtensionMetadata {

	private final FormatConfiguration config;

	public ChatFormatExtension(@NotNull FormatConfiguration config) {
		this.config = config;
	}

	@Override
	public @NotNull String name() {
		return "chat_format";
	}

	@Override
	public @NotNull Class<? extends ChatProcessStage> stage() {
		return FormatStage.class;
	}

	@Override
	public Object configuration() {
		return this.config;
	}
}
