package com.xyrisdev.mist.util.thread;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@UtilityClass
public final class MistExecutors {

	private static ExecutorService io;
	private static ExecutorService processor;

	public static void start() {
		if (io != null || processor != null) {
			return;
		}

		io = Executors.newThreadPerTaskExecutor(
				Thread.ofVirtual()
						.name("mist-io")
						.factory()
		);

		processor = Executors.newSingleThreadExecutor(runnable -> {
			final Thread thread = new Thread(runnable, "mist-processor");

			thread.setDaemon(true);
			return thread;
		});
	}

	public static void shutdown() {
		if (io != null) {
			io.shutdown();
			io = null;
		}

		if (processor != null) {
			processor.shutdown();
			processor = null;
		}
	}

	public static @NotNull ExecutorService io() {
		return io;
	}

	public static @NotNull ExecutorService processor() {
		return processor;
	}
}
