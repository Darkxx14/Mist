package com.xyrisdev.mist.extension;

import com.xyrisdev.mist.MistPaperPlugin;
import com.xyrisdev.mist.api.chat.extension.ExtensionRegistry;
import com.xyrisdev.mist.api.chat.processor.ChatProcessor;
import com.xyrisdev.mist.extension.filter.ChatFilterExtension;
import com.xyrisdev.mist.extension.filter.config.FilterConfiguration;
import com.xyrisdev.mist.extension.filter.config.loader.FilterConfigurationLoader;
import com.xyrisdev.mist.extension.format.ChatFormatExtension;
import com.xyrisdev.mist.extension.format.config.FormatConfiguration;
import com.xyrisdev.mist.extension.format.config.loader.FormatConfigurationLoader;
import com.xyrisdev.mist.extension.render.RenderExtension;
import com.xyrisdev.mist.extension.render.config.RenderConfiguration;
import com.xyrisdev.mist.extension.render.config.loader.RenderConfigurationLoader;
import com.xyrisdev.mist.extension.replacement.ChatReplacementExtension;
import com.xyrisdev.mist.extension.replacement.config.ReplacementsConfiguration;
import com.xyrisdev.mist.extension.replacement.config.loader.ReplacementsConfigurationLoader;
import com.xyrisdev.mist.util.config.ConfigType;
import com.xyrisdev.mist.util.config.registry.ConfigRegistry;
import org.jetbrains.annotations.NotNull;

public class ExtensionManager {

	public static @NotNull ChatProcessor register() {
		final ConfigRegistry registry = MistPaperPlugin.instance().configRegistry();

		final FilterConfiguration filterConfig = FilterConfigurationLoader.load(registry.get(ConfigType.CHAT_FILTER));
		final ReplacementsConfiguration replacementsConfig = ReplacementsConfigurationLoader.load(registry.get(ConfigType.CHAT_REPLACEMENTS));
		final RenderConfiguration renderConfig = RenderConfigurationLoader.load(registry.get(ConfigType.RENDER));
		final FormatConfiguration formatConfig = FormatConfigurationLoader.load(registry.get(ConfigType.CHAT_FORMAT));

		return ExtensionRegistry.create(extensionRegistry -> {
			extensionRegistry.register(1, new ChatFilterExtension(filterConfig));
			extensionRegistry.register(2, new ChatReplacementExtension(replacementsConfig));
			extensionRegistry.register(3, new RenderExtension(renderConfig));
			extensionRegistry.register(100, new ChatFormatExtension(formatConfig));
		});
	}
}