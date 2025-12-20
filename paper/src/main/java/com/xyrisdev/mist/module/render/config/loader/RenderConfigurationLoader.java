package com.xyrisdev.mist.module.render.config.loader;

import com.xyrisdev.library.config.CachableConfiguration;
import com.xyrisdev.mist.module.render.config.RenderConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class RenderConfigurationLoader {

	public static @NotNull RenderConfiguration load(@NotNull CachableConfiguration config) {
		return new RenderConfiguration(
				section(config.getSection("inventory")),
				section(config.getSection("item")),
				section(config.getSection("ender_chest")),
				section(config.getSection("shulker_box"))
		);
	}

	private static @NotNull RenderConfiguration.SectionConfig section(ConfigurationSection section) {
		if (section == null) {
			return RenderConfiguration.SectionConfig.DISABLED;
		}

		return new RenderConfiguration.SectionConfig(
				section.getBoolean("enable", false),
				section.getStringList("prefix"),
				section.getString("processor", ""),
				section.getStringList("hover_text")
		);
	}
}