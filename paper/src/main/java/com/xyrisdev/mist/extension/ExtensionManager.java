package com.xyrisdev.mist.extension;

import com.xyrisdev.mist.Mist;
import com.xyrisdev.mist.api.chat.extension.registry.MistExtensionRegistry;
import com.xyrisdev.mist.api.chat.processor.ChatProcessor;
import com.xyrisdev.mist.config.ConfigType;
import com.xyrisdev.mist.extension.filter.ChatFilterExtension;
import com.xyrisdev.mist.extension.format.ChatFormatExtension;
import com.xyrisdev.mist.extension.mention.ChatMentionExtension;
import com.xyrisdev.mist.extension.render.common.RenderExtension;
import com.xyrisdev.mist.extension.replacement.ChatReplacementExtension;
import com.xyrisdev.mist.util.logger.MistLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public final class ExtensionManager {

	public static @NotNull ChatProcessor register() {
		defaults();

		final ConfigurationSection section = Mist.INSTANCE.config()
											.get(ConfigType.EXTENSION_PRIORITIES)
											.getSection("extensions");

		if (section == null) {
			MistLogger.error("No chat extensions registered due to missing 'extension_priorities.extensions' configuration section");
			return new ChatProcessor(List.of());
		}

		final List<String> sorted = section.getKeys(false)
				.stream()
				.sorted(Comparator.comparingInt(id -> section.getInt(id, Integer.MAX_VALUE)))
				.toList();

		MistLogger.info("Registered " + sorted.size() + " chat extensions");
		return new ChatProcessor(sorted);
	}

	private static void defaults() {
		MistExtensionRegistry.register(new ChatFilterExtension());
		MistExtensionRegistry.register(new ChatReplacementExtension());
		MistExtensionRegistry.register(new ChatMentionExtension());
		MistExtensionRegistry.register(new RenderExtension());
		MistExtensionRegistry.register(new ChatFormatExtension());
	}
}
