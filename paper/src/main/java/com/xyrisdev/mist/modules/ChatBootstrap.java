package com.xyrisdev.mist.modules;

import com.xyrisdev.mist.api.module.builder.ChatModules;
import com.xyrisdev.mist.api.processor.ChatProcessor;
import com.xyrisdev.mist.modules.filter.*;
import com.xyrisdev.mist.util.config.registry.ConfigRegistry;
import org.jetbrains.annotations.NotNull;

public final class ChatBootstrap {

	public static @NotNull ChatProcessor bootstrap(@NotNull ConfigRegistry registry) {
		return new ChatProcessor(
				ChatModules.builder()
						.module(ChatFilterModule::new)
						.register()
						.build()
		);
	}
}
