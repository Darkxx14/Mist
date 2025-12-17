package com.xyrisdev.mist.module.format;

import com.xyrisdev.mist.internal.builder.StageRegistrar;
import com.xyrisdev.mist.internal.module.ChatModule;
import com.xyrisdev.mist.module.format.config.FormatConfiguration;
import com.xyrisdev.mist.module.format.stage.FormatStage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ChatFormatModule implements ChatModule {

	private final FormatConfiguration config;

	private ChatFormatModule(@NotNull FormatConfiguration config) {
		this.config = config;
	}

	@Contract(value = "_ -> new", pure = true)
	public static @NotNull ChatFormatModule create(@NotNull FormatConfiguration config) {
		return new ChatFormatModule(config);
	}

	@Override
	public void register(@NotNull StageRegistrar registrar) {
		registrar.stage(new FormatStage(config), 0);
	}
}
