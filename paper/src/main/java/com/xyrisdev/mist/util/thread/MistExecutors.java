package com.xyrisdev.mist.util.thread;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@UtilityClass
public final class MistExecutors {

	private static ExecutorService IO;
	private static ExecutorService PROCESSOR;

	public static void start() {
		if (IO != null || PROCESSOR != null) {
			return;
		}

		IO = Executors.newThreadPerTaskExecutor(
				Thread.ofVirtual()
						.name("mist-io")
						.factory()
		);

		PROCESSOR = Executors.newSingleThreadExecutor(runnable -> {
			final Thread thread = new Thread(runnable, "mist-processor");

			thread.setDaemon(true);
			return thread;
		});
	}

	public static void shutdown() {
		if (IO != null) {
			IO.shutdown();
			IO = null;
		}

		if (PROCESSOR != null) {
			PROCESSOR.shutdown();
			PROCESSOR = null;
		}
	}

	public static @NotNull ExecutorService io() {
		return IO;
	}

	public static @NotNull ExecutorService processor() {
		return PROCESSOR;
	}
}
