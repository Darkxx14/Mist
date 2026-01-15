package com.xyrisdev.mist.extension.format.config;

import com.xyrisdev.mist.extension.format.entry.FormatEntry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record FormatConfiguration(
		@NotNull Map<String, FormatEntry> formats,
		@NotNull String fallback
) {

	public FormatEntry resolve(@NotNull String group) {
		return formats.getOrDefault(group, formats.get(fallback));
	}
}