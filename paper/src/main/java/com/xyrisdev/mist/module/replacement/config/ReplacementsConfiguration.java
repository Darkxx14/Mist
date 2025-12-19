package com.xyrisdev.mist.module.replacement.config;

import com.xyrisdev.mist.module.replacement.entry.UnifiedReplacement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ReplacementsConfiguration(
		@NotNull List<UnifiedReplacement> replacements
) {}