package com.xyrisdev.mist.module;

import com.xyrisdev.mist.MistPaperPlugin;
import com.xyrisdev.mist.api.processor.ChatProcessor;
import com.xyrisdev.mist.internal.builder.MistProcessorBuilder;
import com.xyrisdev.mist.module.filter.ChatFilterModule;
import com.xyrisdev.mist.module.filter.config.FilterConfiguration;
import com.xyrisdev.mist.module.filter.config.loader.FilterConfigurationLoader;
import com.xyrisdev.mist.module.format.ChatFormatModule;
import com.xyrisdev.mist.module.format.config.FormatConfiguration;
import com.xyrisdev.mist.module.format.config.loader.FormatConfigurationLoader;
import com.xyrisdev.mist.module.render.RenderModule;
import com.xyrisdev.mist.module.render.config.RenderConfiguration;
import com.xyrisdev.mist.module.render.config.loader.RenderConfigurationLoader;
import com.xyrisdev.mist.module.replacement.ChatReplacementModule;
import com.xyrisdev.mist.module.replacement.config.ReplacementsConfiguration;
import com.xyrisdev.mist.module.replacement.config.loader.ReplacementsConfigurationLoader;
import com.xyrisdev.mist.util.config.ConfigType;
import com.xyrisdev.mist.util.config.registry.ConfigRegistry;
import org.jetbrains.annotations.NotNull;

public final class ModuleRegistrar {

	public static @NotNull ChatProcessor build() {
		final ConfigRegistry registry = MistPaperPlugin.instance().configRegistry();
		final FilterConfiguration filterConfig = FilterConfigurationLoader.load(registry.get(ConfigType.CHAT_FILTER));
		final FormatConfiguration formatConfig = FormatConfigurationLoader.load(registry.get(ConfigType.CHAT_FORMAT));
		final ReplacementsConfiguration replacementsConfig = ReplacementsConfigurationLoader.load(registry.get(ConfigType.CHAT_REPLACEMENTS));
		final RenderConfiguration renderConfig = RenderConfigurationLoader.load(registry.get(ConfigType.RENDER));

		return new MistProcessorBuilder()
				.group(0, filter -> filter.module(ChatFilterModule.create(filterConfig)))
				.group(1, replacements -> replacements.module(ChatReplacementModule.create(replacementsConfig)))
				.group(2, render -> render.module(RenderModule.create(renderConfig)))
				.group(100, format -> format.module(ChatFormatModule.create(formatConfig)))
				.build();
	}
}