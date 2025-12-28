package com.xyrisdev.mist.util.thread;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@UtilityClass
@SuppressWarnings("CallToPrintStackTrace")
public class MistExecutors {

    private static ExecutorService IO;
    private static ExecutorService PROCESSOR;

    public static void start() {
        if (IO != null || PROCESSOR != null) {
            return;
        }

        IO = Executors.newSingleThreadExecutor(runnable -> {
            final Thread thread = new Thread(runnable, "mist-io");

            thread.setDaemon(true);
            return thread;
        });

        PROCESSOR = Executors.newSingleThreadExecutor(runnable -> {
           final  Thread thread = new Thread(runnable, "mist-processor");

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

    public static @NotNull ExecutorHandle io() {
        return task -> IO.execute(wrap(task));
    }

    public static @NotNull ExecutorHandle processor() {
        return task -> PROCESSOR.execute(wrap(task));
    }

    @Contract(pure = true)
    private static @NotNull Runnable wrap(Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        };
    }

    @FunctionalInterface
    public interface ExecutorHandle {
        void execute(@NotNull Runnable task);
    }
}
