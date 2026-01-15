package com.xyrisdev.mist.extension.render.config;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record RenderConfiguration(
		@NotNull SectionConfig inventory,
		@NotNull SectionConfig item,
		@NotNull SectionConfig enderChest,
		@NotNull SectionConfig shulkerBox,
		@NotNull SectionConfig command
) {

	public record SectionConfig(
			boolean enabled,
			@NotNull List<String> prefix,
			@NotNull String processor,
			@NotNull List<String> hoverText
	) {
		public static final SectionConfig DISABLED = new SectionConfig(false, List.of(), "", List.of());
	}
}