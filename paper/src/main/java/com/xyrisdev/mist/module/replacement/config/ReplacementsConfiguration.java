package com.xyrisdev.mist.module.replacement.config;

import com.xyrisdev.mist.module.replacement.entry.PlaceholderAPIReplacement;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record ReplacementsConfiguration(
		@NotNull Map<String, String> replacements,
		@NotNull Map<String, PlaceholderAPIReplacement> papiReplacements
) {}
