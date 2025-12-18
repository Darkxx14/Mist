package com.xyrisdev.mist.module;

import com.xyrisdev.mist.MistPaperPlugin;
import com.xyrisdev.mist.api.processor.ChatProcessor;
import com.xyrisdev.mist.internal.builder.MistProcessorBuilder;
import com.xyrisdev.mist.module.format.ChatFormatModule;
import com.xyrisdev.mist.module.format.config.FormatConfiguration;
import com.xyrisdev.mist.module.format.config.loader.FormatConfigurationLoader;
import com.xyrisdev.mist.module.replacement.ChatReplacementsModule;
import com.xyrisdev.mist.module.replacement.config.ReplacementsConfiguration;
import com.xyrisdev.mist.module.replacement.config.loader.ReplacementsConfigurationLoader;
import com.xyrisdev.mist.util.config.ConfigType;
import com.xyrisdev.mist.util.config.registry.ConfigRegistry;
import org.jetbrains.annotations.NotNull;

public final class ModuleRegistrar {

	public static @NotNull ChatProcessor build() {
		final ConfigRegistry registry = MistPaperPlugin.instance().configRegistry();
		final FormatConfiguration formatConfig = FormatConfigurationLoader.load(registry.get(ConfigType.CHAT_FORMAT));
		final ReplacementsConfiguration replacementsConfig = ReplacementsConfigurationLoader.load(registry.get(ConfigType.CHAT_REPLACEMENTS));

		return new MistProcessorBuilder()
				.group(0, replacements -> replacements.module(ChatReplacementsModule.create(replacementsConfig)))
				.group(100, format -> format.module(ChatFormatModule.create(formatConfig)))
				.build();
	}
}
