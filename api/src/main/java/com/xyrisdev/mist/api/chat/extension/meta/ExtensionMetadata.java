package com.xyrisdev.mist.api.chat.extension.meta;

import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessStage;
import org.jetbrains.annotations.NotNull;

// todo: maybe remove configuration field
public interface ExtensionMetadata {

	@NotNull String name();

	@NotNull Class<? extends ChatProcessStage> stage();

	default Object configuration() {
		return null;
	}
}