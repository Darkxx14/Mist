package com.xyrisdev.mist.module.replacement;

import com.xyrisdev.mist.internal.builder.StageRegistrar;
import com.xyrisdev.mist.internal.module.ChatModule;
import com.xyrisdev.mist.module.replacement.config.ReplacementsConfiguration;
import com.xyrisdev.mist.module.replacement.stage.ReplacementStage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ChatReplacementsModule implements ChatModule {

	private final ReplacementsConfiguration config;

	private ChatReplacementsModule(@NotNull ReplacementsConfiguration config) {
		this.config = config;
	}

	@Contract(value = "_ -> new", pure = true)
	public static @NotNull ChatReplacementsModule create(@NotNull ReplacementsConfiguration config) {
		return new ChatReplacementsModule(config);
	}

	@Override
	public void register(@NotNull StageRegistrar registrar) {
		registrar.stage(new ReplacementStage(config), 0);
	}
}