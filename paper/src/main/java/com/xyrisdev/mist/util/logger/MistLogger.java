package com.xyrisdev.mist.util.logger;

import com.xyrisdev.library.logger.XLogger;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@UtilityClass
public class MistLogger {

	private static final String PREFIX = "<gradient:#E03261:#811937><bold>Mist</bold></gradient> <dark_gray>·</dark_gray> ";

	public static void infoNoPrefix(@NotNull String message) {
		XLogger.custom().info(formatNoPrefix(message));
	}

	public static void info(@NotNull String message) {
		XLogger.custom().info(format(message));
	}

	public static void info(@NotNull String scope, @NotNull String message) {
		XLogger.custom().info(format(scope, message));
	}

	public static void warn(@NotNull String message) {
		XLogger.custom().warn(format(message));
	}

	public static void warn(@NotNull String scope, @NotNull String message) {
		XLogger.custom().warn(format(scope, message));
	}

	public static void error(@NotNull String message) {
		XLogger.custom().error(format(message));
	}

	public static void error(@NotNull String scope, @NotNull String message) {
		XLogger.custom().error(format(scope, message));
	}

	public static void error(@NotNull String message, @NotNull Throwable t) {
		XLogger.custom().error(format(message));
		XLogger.stackTrace(t);
	}

	public static void debug(@NotNull Supplier<String> supplier) {
		XLogger.custom().debug(format(supplier.get()));
	}

	public static void debug(@NotNull String scope, @NotNull Supplier<String> supplier) {
		XLogger.custom().debug(format(scope, supplier.get()));
	}

	private static @NotNull String formatNoPrefix(@NotNull String message) {
		return message;
	}

	private static @NotNull String format(@NotNull String message) {
		return PREFIX + message;
	}

	private static @NotNull String format(@NotNull String scope, @NotNull String message) {
		return PREFIX + "<aqua>[" + scope + "]</aqua> " + message;
	}
}
