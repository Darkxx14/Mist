package com.xyrisdev.mist.module.render;

import com.xyrisdev.mist.internal.builder.StageRegistrar;
import com.xyrisdev.mist.internal.module.ChatModule;
import com.xyrisdev.mist.module.render.config.RenderConfiguration;
import com.xyrisdev.mist.module.render.stage.RenderStage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class RenderModule implements ChatModule {

	private final RenderConfiguration config;

	private RenderModule(@NotNull RenderConfiguration config) {
		this.config = config;
	}

	@Contract(value = "_ -> new", pure = true)
	public static @NotNull RenderModule create(@NotNull RenderConfiguration config) {
		return new RenderModule(config);
	}

	@Override
	public void register(@NotNull StageRegistrar registrar) {
		registrar.stage(new RenderStage(config), 2);
	}
}