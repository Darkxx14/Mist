package com.xyrisdev.mist.command.internal;

import com.xyrisdev.mist.command.MistCallbackCommand;
import com.xyrisdev.mist.command.MistCommand;
import com.xyrisdev.mist.command.internal.exception.HandledParseException;
import org.bukkit.plugin.Plugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PaperSimpleSenderMapper;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.NotNull;

public class MistCommandManager {

	private static PaperCommandManager<Source> commandManager;

	public static void of(@NotNull Plugin plugin) {
		if (commandManager != null) {
			throw new IllegalStateException("MistCommandManager already initialized");
		}

		commandManager = PaperCommandManager
				.builder(PaperSimpleSenderMapper.simpleSenderMapper())
				.executionCoordinator(ExecutionCoordinator.asyncCoordinator())
				.buildOnEnable(plugin);

		commandManager.exceptionController()
				.registerHandler(
						HandledParseException.class,
						context -> context.exception().runnable().run()
				);

		commands();
	}

	public static @NotNull PaperCommandManager<Source> manager() {
		if (commandManager == null) {
			throw new IllegalStateException("MistCommandManager not initialized yet");
		}

		return commandManager;
	}

	private static void commands() {
		MistCommand.register();
		MistCallbackCommand.register();
	}
}
