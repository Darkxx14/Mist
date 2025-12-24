package com.xyrisdev.mist.extension;

import com.xyrisdev.mist.MistPaperPlugin;
import com.xyrisdev.mist.api.chat.processor.ChatProcessor;
import com.xyrisdev.mist.api.chat.processor.stage.ChatProcessorStage;
import com.xyrisdev.mist.chat.extension.MistChatProcessorRegistrar;
import com.xyrisdev.mist.chat.processor.builder.ChatProcessorBuilder;
import com.xyrisdev.mist.extension.filter.config.FilterConfiguration;
import com.xyrisdev.mist.extension.filter.config.loader.FilterConfigurationLoader;
import com.xyrisdev.mist.extension.filter.stage.FilterStage;
import com.xyrisdev.mist.extension.format.config.FormatConfiguration;
import com.xyrisdev.mist.extension.format.config.loader.FormatConfigurationLoader;
import com.xyrisdev.mist.extension.format.stage.FormatStage;
import com.xyrisdev.mist.extension.render.config.RenderConfiguration;
import com.xyrisdev.mist.extension.render.config.loader.RenderConfigurationLoader;
import com.xyrisdev.mist.extension.render.stage.RenderStage;
import com.xyrisdev.mist.extension.replacement.config.ReplacementsConfiguration;
import com.xyrisdev.mist.extension.replacement.config.loader.ReplacementsConfigurationLoader;
import com.xyrisdev.mist.extension.replacement.stage.ReplacementStage;
import com.xyrisdev.mist.util.config.ConfigType;
import com.xyrisdev.mist.util.config.registry.ConfigRegistry;
import org.jetbrains.annotations.NotNull;

public class ExtensionRegistrar {

	private static MistChatProcessorRegistrar registrar;

	public static @NotNull ChatProcessor build() {
		final ConfigRegistry registry = MistPaperPlugin.instance().configRegistry();
		final ChatProcessorBuilder builder = new ChatProcessorBuilder();

		registrar = new MistChatProcessorRegistrar(builder);

		final FilterConfiguration filterConfig = FilterConfigurationLoader.load(registry.get(ConfigType.CHAT_FILTER));
		final ReplacementsConfiguration replacementsConfig = ReplacementsConfigurationLoader.load(registry.get(ConfigType.CHAT_REPLACEMENTS));
		final RenderConfiguration renderConfig = RenderConfigurationLoader.load(registry.get(ConfigType.RENDER));
		final FormatConfiguration formatConfig = FormatConfigurationLoader.load(registry.get(ConfigType.CHAT_FORMAT));

		registrar.register(1, new FilterStage(filterConfig));
		registrar.register(2, new ReplacementStage(replacementsConfig));
		registrar.register(3, new RenderStage(renderConfig));
		registrar.register(100, new FormatStage(formatConfig));

		return builder.build();
	}

	public static void register(int order, @NotNull ChatProcessorStage stage) {
		if (registrar == null) {
			throw new IllegalStateException("ExtensionRegistrar not initialized");
		}

		registrar.register(order, stage);
	}
}