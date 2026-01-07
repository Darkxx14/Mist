package com.xyrisdev.mist.util.thread;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
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

        IO = create("mist-io");
        PROCESSOR = create("mist-processor");
    }

    public static void shutdown() {
        shutdown(IO);
        shutdown(PROCESSOR);

        IO = null;
        PROCESSOR = null;
    }

    public static @NotNull ExecutorService io() {
        return IO;
    }

    public static @NotNull ExecutorService processor() {
        return PROCESSOR;
    }

    @Contract("_ -> new")
    private static @NotNull ExecutorService create(String name) {
        return Executors.newSingleThreadExecutor(runnable -> {
           final Thread thread = new Thread(runnable, name);

            thread.setDaemon(true);
            return thread;
        });
    }

    private static void shutdown(ExecutorService service) {
        if (service != null) {
            service.shutdown();
        }
    }
}
