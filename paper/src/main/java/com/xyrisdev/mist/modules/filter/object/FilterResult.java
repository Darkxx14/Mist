package com.xyrisdev.mist.modules.filter.object;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record FilterResult(
		@NotNull FilterDecision decision,
		@Nullable String modifiedMessage
) {

	@Contract(" -> new")
	public static @NotNull FilterResult pass() {
		return new FilterResult(FilterDecision.PASS, null);
	}

	public static @NotNull FilterResult block() {
		return new FilterResult(FilterDecision.BLOCK, null);
	}

	public static @NotNull FilterResult modify(@NotNull String message) {
		return new FilterResult(FilterDecision.MODIFY, message);
	}
}
