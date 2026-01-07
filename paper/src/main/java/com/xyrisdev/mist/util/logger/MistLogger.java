package com.xyrisdev.mist.util.logger;

import com.xyrisdev.library.logger.XLogger;
import lombok.experimental.UtilityClass;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@UtilityClass
public class MistLogger {

	private static final String prefix = "<gradient:#E03261:#811937><bold>Mist</bold></gradient> <dark_gray>·</dark_gray> ";
	private static final String gradient = "<gradient:#E03261:#811937><bold>";
	private static final String header_reset = "</bold></gradient>";

	public static void startup(@NotNull String version, @NotNull Server server) {
		infoNoPrefix("");

		infoNoPrefix(gradient + "   __  __  " + header_reset);
		infoNoPrefix(
				gradient + "  |  \\/  | " + header_reset +
						"<gray>Mist v" + version + "</gray>"
		);
		infoNoPrefix(
				gradient + "  | |\\/| | " + header_reset +
						"<gray>Running on " + server.getName()
						+ " - " + server.getBukkitVersion() + "</gray>"
		);

		infoNoPrefix("");
	}

	public static void infoNoPrefix(@NotNull String message) {
		XLogger.custom().info(message);
	}

	public static void info(@NotNull String message) {
		XLogger.custom().info(format(message));
	}

	public static void warn(@NotNull String message) {
		XLogger.custom().warn(format(message));
	}

	public static void error(@NotNull String message) {
		XLogger.custom().error(format(message));
	}

	public static void error(@NotNull String message, @NotNull Throwable t) {
		XLogger.custom().error(format(message));
		XLogger.stackTrace(t);
	}

	public static void debug(@NotNull Supplier<String> supplier) {
		XLogger.custom().debug(format(supplier.get()));
	}

	private static @NotNull String format(@NotNull String message) {
		return prefix + message;
	}
}
