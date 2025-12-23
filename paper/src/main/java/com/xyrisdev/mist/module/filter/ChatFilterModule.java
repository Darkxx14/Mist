package com.xyrisdev.mist.module.filter;

import com.xyrisdev.mist.internal.builder.StageRegistrar;
import com.xyrisdev.mist.internal.module.ChatModule;
import com.xyrisdev.mist.module.filter.config.FilterConfiguration;
import com.xyrisdev.mist.module.filter.stage.FilterStage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ChatFilterModule implements ChatModule {

	private final FilterConfiguration config;

	private ChatFilterModule(@NotNull FilterConfiguration config) {
		this.config = config;
	}

	@Contract(value = "_ -> new", pure = true)
	public static @NotNull ChatFilterModule create(@NotNull FilterConfiguration config) {
		return new ChatFilterModule(config);
	}

	@Override
	public void register(@NotNull StageRegistrar registrar) {
		registrar.stage(new FilterStage(config), 1);
	}
}