package com.xyrisdev.mist.command.internal.exception;

import org.jetbrains.annotations.NotNull;

public class HandledParseException extends RuntimeException {

	private final transient @NotNull Runnable runnable;

	private HandledParseException(@NotNull Runnable runnable) {
		this.runnable = runnable;
	}

	public static @NotNull HandledParseException handle(@NotNull Runnable runnable) {
		return new HandledParseException(runnable);
	}

	public @NotNull Runnable runnable() {
		return runnable;
	}
}
