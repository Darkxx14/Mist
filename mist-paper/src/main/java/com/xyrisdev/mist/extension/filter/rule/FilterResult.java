package com.xyrisdev.mist.extension.filter.rule;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record FilterResult(
		boolean cancel,
		String modifiedMessage
) {

	@Contract(" -> new")
	public static @NotNull FilterResult pass() {
		return new FilterResult(false, null);
	}

	@Contract(" -> new")
	public static @NotNull FilterResult cancelled() {
		return new FilterResult(true, null);
	}

	@Contract("_ -> new")
	public static @NotNull FilterResult modify(@NotNull String message) {
		return new FilterResult(false, message);
	}
}
