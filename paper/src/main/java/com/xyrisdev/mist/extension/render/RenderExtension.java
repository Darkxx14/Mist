package com.xyrisdev.mist.extension.render;

import com.xyrisdev.mist.api.chat.extension.meta.ExtensionMetadata;
import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessStage;
import com.xyrisdev.mist.extension.render.config.RenderConfiguration;
import com.xyrisdev.mist.extension.render.stage.RenderStage;
import org.jetbrains.annotations.NotNull;

public class RenderExtension implements ExtensionMetadata {

	private final RenderConfiguration config;

	public RenderExtension(@NotNull RenderConfiguration config) {
		this.config = config;
	}

	@Override
	public @NotNull String name() {
		return "render";
	}

	@Override
	public @NotNull Class<? extends ChatProcessStage> stage() {
		return RenderStage.class;
	}

	@Override
	public Object configuration() {
		return this.config;
	}
}
