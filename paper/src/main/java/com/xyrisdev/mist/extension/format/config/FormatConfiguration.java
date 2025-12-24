package com.xyrisdev.mist.extension.format.config;

import com.xyrisdev.mist.extension.format.entry.FormatEntry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class FormatConfiguration {

	private final Map<String, FormatEntry> formats;
	private final String fallback;

	public FormatConfiguration(@NotNull Map<String, FormatEntry> formats, @NotNull String fallback) {
		this.formats = formats;
		this.fallback = fallback;
	}

	public FormatEntry resolve(String group) {
		return formats.getOrDefault(group, formats.get(fallback));
	}
}