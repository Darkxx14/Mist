package com.xyrisdev.mist.extension.replacement.config;

import com.xyrisdev.mist.extension.replacement.entry.UnifiedReplacement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ReplacementsConfiguration(
		@NotNull List<UnifiedReplacement> replacements
) {}