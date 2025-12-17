package com.xyrisdev.mist.module;

import com.xyrisdev.mist.MistPaperPlugin;
import com.xyrisdev.mist.api.processor.ChatProcessor;
import com.xyrisdev.mist.internal.builder.MistProcessorBuilder;
import com.xyrisdev.mist.module.format.ChatFormatModule;
import com.xyrisdev.mist.module.format.config.FormatConfiguration;
import com.xyrisdev.mist.module.format.config.loader.FormatConfigurationLoader;
import com.xyrisdev.mist.util.config.ConfigType;
import com.xyrisdev.mist.util.config.registry.ConfigRegistry;
import org.jetbrains.annotations.NotNull;

public final class ModuleRegistrar {

	public static @NotNull ChatProcessor build() {
		final ConfigRegistry registry = MistPaperPlugin.instance().configRegistry();
		final FormatConfiguration formatConfig = FormatConfigurationLoader.load(registry.get(ConfigType.CHAT_FORMAT));

		return new MistProcessorBuilder()
				.group(0, format -> format.module(ChatFormatModule.create(formatConfig)))
				.build();
	}
}
