package com.xyrisdev.mist.util.text.cache;

import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

public record TextCacheKey(
		@NotNull Audience audience,
		@NotNull String message
) {}
