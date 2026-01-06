package com.xyrisdev.mist.extension;

import com.xyrisdev.mist.ChatPlugin;
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
import com.xyrisdev.mist.config.ConfigType;
import com.xyrisdev.mist.config.registry.ConfigRegistry;
import com.xyrisdev.mist.util.logger.MistLogger;
import org.jetbrains.annotations.NotNull;

public class ExtensionManager {

	public static @NotNull ChatProcessor register() {
		final ConfigRegistry config = ChatPlugin.instance().configRegistry();

		final FilterConfiguration filterConfig = FilterConfigurationLoader.load(config.get(ConfigType.CHAT_FILTER));
		final ReplacementsConfiguration replacementsConfig = ReplacementsConfigurationLoader.load(config.get(ConfigType.CHAT_REPLACEMENTS));
		final RenderConfiguration renderConfig = RenderConfigurationLoader.load(config.get(ConfigType.RENDER));
		final FormatConfiguration formatConfig = FormatConfigurationLoader.load(config.get(ConfigType.CHAT_FORMAT));

		final ExtensionRegistry registry = new ExtensionRegistry();

		registry.register(1, new ChatFilterExtension(filterConfig));
		registry.register(2, new ChatReplacementExtension(replacementsConfig));
		registry.register(3, new RenderExtension(renderConfig));
		registry.register(100, new ChatFormatExtension(formatConfig));

		final ChatProcessor processor = registry.build();

		MistLogger.info("Registered " + registry.size() + " chat extensions");
		return processor;
	}
}