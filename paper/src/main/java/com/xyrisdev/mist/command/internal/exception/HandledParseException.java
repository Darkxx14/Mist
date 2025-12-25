package com.xyrisdev.mist.command.internal.exception;

import org.jetbrains.annotations.NotNull;

public class HandledParseException extends RuntimeException {

	private final @NotNull Runnable runnable;

	private HandledParseException(@NotNull String input, @NotNull Runnable runnable) {
		this.runnable = runnable;
	}

	public static @NotNull HandledParseException handle(@NotNull String input, @NotNull Runnable runnable) {
		return new HandledParseException(input, runnable);
	}

	public @NotNull Runnable runnable() {
		return runnable;
	}
}
