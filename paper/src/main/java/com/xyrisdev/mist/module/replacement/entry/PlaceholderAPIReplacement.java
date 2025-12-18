package com.xyrisdev.mist.module.replacement.entry;

import org.jetbrains.annotations.NotNull;

public record PlaceholderAPIReplacement(
		@NotNull String key,
		@NotNull String processor,
		boolean requirePermission,
		@NotNull String permission
) {}
