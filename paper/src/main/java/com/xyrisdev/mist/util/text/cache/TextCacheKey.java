package com.xyrisdev.mist.util.text.cache;

import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

public record TextCacheKey(
		@NotNull Audience audience,
		@NotNull String message
) {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof TextCacheKey other)) {
			return false;
		}

		return audience == other.audience && message.equals(other.message);
	}
}
